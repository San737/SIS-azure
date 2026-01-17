# Simple API Speed Tester
$baseUrl = "http://localhost:8080/api/v1"

Write-Host "`n========== API Speed Test ==========" -ForegroundColor Cyan

# Test public endpoints
Write-Host "`n--- Public Endpoints ---" -ForegroundColor Yellow
Write-Host "Get Colleges..." -NoNewline
$time1 = Measure-Command { Invoke-WebRequest "$baseUrl/public/colleges" -UseBasicParsing } | Select-Object -ExpandProperty TotalMilliseconds
Write-Host " ${time1}ms" -ForegroundColor $(if ($time1 -lt 500) { "Green" } else { "Red" })

Write-Host "Get Departments..." -NoNewline
$time2 = Measure-Command { Invoke-WebRequest "$baseUrl/public/departments" -UseBasicParsing } | Select-Object -ExpandProperty TotalMilliseconds
Write-Host " ${time2}ms" -ForegroundColor $(if ($time2 -lt 500) { "Green" } else { "Red" })

# Login and get token
Write-Host "`n--- Authentication ---" -ForegroundColor Yellow
Write-Host "Student Login..." -NoNewline
$loginBody = '{"email":"san@rnsit.ac.in","password":"student123"}'
$loginTime = Measure-Command {
    $loginResp = Invoke-WebRequest -Uri "$baseUrl/auth/login" -Method POST -Body $loginBody -ContentType "application/json" -UseBasicParsing
} | Select-Object -ExpandProperty TotalMilliseconds
Write-Host " ${loginTime}ms" -ForegroundColor $(if ($loginTime -lt 500) { "Green" } else { "Red" })

try {
    $token = ($loginResp.Content | ConvertFrom-Json).token
    $headers = @{ Authorization = "Bearer $token" }
    
    # Test authenticated endpoints
    Write-Host "`n--- Student Endpoints (Authenticated) ---" -ForegroundColor Yellow
    
    Write-Host "Dashboard..." -NoNewline
    $dashTime = Measure-Command { Invoke-WebRequest "$baseUrl/student/dashboard" -Headers $headers -UseBasicParsing } | Select-Object -ExpandProperty TotalMilliseconds
    Write-Host " ${dashTime}ms" -ForegroundColor $(if ($dashTime -lt 500) { "Green" } else { "Red" })
    
    Write-Host "Profile..." -NoNewline
    $profTime = Measure-Command { Invoke-WebRequest "$baseUrl/student/profile" -Headers $headers -UseBasicParsing } | Select-Object -ExpandProperty TotalMilliseconds
    Write-Host " ${profTime}ms" -ForegroundColor $(if ($profTime -lt 500) { "Green" } else { "Red" })
    
    Write-Host "Courses..." -NoNewline
    $coursesTime = Measure-Command { Invoke-WebRequest "$baseUrl/student/courses" -Headers $headers -UseBasicParsing } | Select-Object -ExpandProperty TotalMilliseconds
    Write-Host " ${coursesTime}ms" -ForegroundColor $(if ($coursesTime -lt 500) { "Green" } else { "Red" })
    
    Write-Host "Enrollments..." -NoNewline
    $enrollTime = Measure-Command { Invoke-WebRequest "$baseUrl/student/enrollments" -Headers $headers -UseBasicParsing } | Select-Object -ExpandProperty TotalMilliseconds
    Write-Host " ${enrollTime}ms" -ForegroundColor $(if ($enrollTime -lt 500) { "Green" } else { "Red" })
    
    # Calculate average
    $avgTime = ($time1 + $time2 + $loginTime + $dashTime + $profTime + $coursesTime + $enrollTime) / 7
    
    Write-Host "`n========== Summary ==========" -ForegroundColor Cyan
    Write-Host "Average Response Time: $([math]::Round($avgTime, 2))ms" -ForegroundColor $(if ($avgTime -lt 300) { "Green" } elseif ($avgTime -lt 1000) { "Yellow" } else { "Red" })
    
    if ($avgTime -gt 1000) {
        Write-Host "`nPERFORMANCE ISSUE DETECTED!" -ForegroundColor Red
        Write-Host "Recommendations:" -ForegroundColor Yellow
        Write-Host "  1. Check database query performance"
        Write-Host "  2. Add database indexes on foreign keys"
        Write-Host "  3. Enable query logging to find slow queries"
        Write-Host "  4. Look for N+1 query problems"
    }
    
} catch {
    Write-Host "`nError: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""
