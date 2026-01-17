# API Speed Testing Script
# Tests all major endpoints and reports response times

$baseUrl = "http://localhost:8080/api/v1"

Write-Host "`n=== API Speed Test ===" -ForegroundColor Cyan
Write-Host "Testing endpoints at: $baseUrl`n" -ForegroundColor Yellow

# Function to test an endpoint
function Test-Endpoint {
    param (
        [string]$Name,
        [string]$Url,
        [string]$Method = "GET",
        [hashtable]$Headers = @{},
        [string]$Body = $null
    )
    
    Write-Host "Testing: $Name..." -NoNewline
    
    try {
        $result = Measure-Command {
            if ($Body) {
                $response = Invoke-WebRequest -Uri $Url -Method $Method -Headers $Headers -Body $Body -ContentType "application/json" -UseBasicParsing -ErrorAction Stop
            } else {
                $response = Invoke-WebRequest -Uri $Url -Method $Method -Headers $Headers -UseBasicParsing -ErrorAction Stop
            }
        }
        
        $timeMs = [math]::Round($result.TotalMilliseconds, 2)
        Write-Host " OK ${timeMs}ms" -ForegroundColor Green
        
        return [pscustomobject]@{
            Name = $Name
            Time = $timeMs
            Status = "Success"
        }
    } catch {
        $errorMsg = $_.Exception.Message
        Write-Host " FAIL: $errorMsg" -ForegroundColor Red
        
        return [pscustomobject]@{
            Name = $Name
            Time = 0
            Status = "Failed: $errorMsg"
        }
    }
}

$results = @()

# 1. Public Endpoints (No Auth)
Write-Host "`n--- Public Endpoints ---" -ForegroundColor Cyan
$results += Test-Endpoint -Name "Get Colleges" -Url "$baseUrl/public/colleges"
$results += Test-Endpoint -Name "Get Departments" -Url "$baseUrl/public/departments"

# 2. Authentication
Write-Host "`n--- Authentication ---" -ForegroundColor Cyan
$loginBody = @{
    email = "san@rnsit.ac.in"
    password = "student123"
} | ConvertTo-Json

$loginResult = Test-Endpoint -Name "Student Login" -Url "$baseUrl/auth/login" -Method "POST" -Body $loginBody
$results += $loginResult

# Extract token from login response if successful
$token = $null
if ($loginResult.Status -eq "Success") {
    try {
        $loginResponse = Invoke-WebRequest -Uri "$baseUrl/auth/login" -Method POST -Body $loginBody -ContentType "application/json" -UseBasicParsing
        $tokenData = $loginResponse.Content | ConvertFrom-Json
        $token = $tokenData.token
        Write-Host "  Token obtained successfully" -ForegroundColor Green
    } catch {
        Write-Host "  Could not extract token" -ForegroundColor Yellow
    }
}

# 3. Student Endpoints (With Auth)
if ($token) {
    Write-Host "`n--- Student Endpoints (Authenticated) ---" -ForegroundColor Cyan
    $authHeaders = @{ Authorization = "Bearer $token" }
    
    $results += Test-Endpoint -Name "Student Dashboard" -Url "$baseUrl/student/dashboard" -Headers $authHeaders
    $results += Test-Endpoint -Name "Student Profile" -Url "$baseUrl/student/profile" -Headers $authHeaders
    $results += Test-Endpoint -Name "Available Courses" -Url "$baseUrl/student/courses" -Headers $authHeaders
    $results += Test-Endpoint -Name "Student Enrollments" -Url "$baseUrl/student/enrollments" -Headers $authHeaders
} else {
    Write-Host "`n--- Skipping Student Endpoints (No token) ---" -ForegroundColor Yellow
}

# Summary Report
Write-Host "`n=== Summary ===" -ForegroundColor Cyan
$successfulTests = $results | Where-Object { $_.Status -eq "Success" }
$failedTests = $results | Where-Object { $_.Status -ne "Success" }

if ($successfulTests.Count -gt 0) {
    $avgTime = [math]::Round(($successfulTests | Measure-Object -Property Time -Average).Average, 2)
    $maxTime = ($successfulTests | Measure-Object -Property Time -Maximum).Maximum
    $minTime = ($successfulTests | Measure-Object -Property Time -Minimum).Minimum
    
    Write-Host "`nSuccessful Tests: $($successfulTests.Count)/$($results.Count)" -ForegroundColor Green
    Write-Host "Average Response Time: ${avgTime}ms" -ForegroundColor $(if ($avgTime -lt 100) { "Green" } elseif ($avgTime -lt 500) { "Yellow" } else { "Red" })
    Write-Host "Fastest: ${minTime}ms" -ForegroundColor Green
    Write-Host "Slowest: ${maxTime}ms" -ForegroundColor $(if ($maxTime -lt 500) { "Yellow" } else { "Red" })
}

if ($failedTests.Count -gt 0) {
    Write-Host "`nFailed Tests: $($failedTests.Count)" -ForegroundColor Red
    foreach ($test in $failedTests) {
        Write-Host "  - $($test.Name): $($test.Status)" -ForegroundColor Red
    }
}

# Performance Rating
if ($successfulTests.Count -gt 0) {
    Write-Host "`nPerformance Rating:" -NoNewline
    if ($avgTime -lt 100) {
        Write-Host " Excellent" -ForegroundColor Green
    } elseif ($avgTime -lt 300) {
        Write-Host " Good" -ForegroundColor Green
    } elseif ($avgTime -lt 500) {
        Write-Host " Acceptable" -ForegroundColor Yellow
    } elseif ($avgTime -lt 1000) {
        Write-Host " Slow" -ForegroundColor Yellow
    } else {
        Write-Host " Very Slow" -ForegroundColor Red
        Write-Host "`nRecommendations:" -ForegroundColor Yellow
        Write-Host "  - Check database query performance" -ForegroundColor White
        Write-Host "  - Add database indexes" -ForegroundColor White
        Write-Host "  - Enable database query logging" -ForegroundColor White
        Write-Host "  - Check for N+1 query problems" -ForegroundColor White
    }
}

Write-Host ""
