# Azure Deployment (SIS)

This document summarizes the exact Azure deployment configuration used for the SIS demo.

## Services Used

- Azure App Service (Backend, Java 17)
- Azure Database for MySQL Flexible Server
- Azure Static Web Apps (Frontend)

## Backend (App Service)

**App name:** sis-backend-server

**GitHub Actions workflow:**

- File: [.github/workflows/backend.yml](.github/workflows/backend.yml)
- Build uses: `mvn -f Backend/pom.xml clean install -DskipTests`

**Environment variables (App Service → Configuration → Application settings):**

- SPRING_DATASOURCE_URL = jdbc:mysql://sis-mysql-server.mysql.database.azure.com:3306/sis_db?useSSL=true&requireSSL=true&serverTimezone=UTC
- SPRING_DATASOURCE_USERNAME = sisadmin
- SPRING_DATASOURCE_PASSWORD = Qwert@1234
- APP_CORS_ALLOWED_ORIGINS = https://thankful-coast-07aa3a700.6.azurestaticapps.net

**Backend test URL:**

- https://sis-backend-server.azurewebsites.net/api/v1/public/colleges

## Database (Azure MySQL Flexible Server)

**Server host:** sis-mysql-server.mysql.database.azure.com

**Database name:** sis_db

**Port:** 3306

**Admin user:** sisadmin

**Notes:**

- Ensure public access is enabled.
- Ensure your IP is whitelisted in firewall.
- Import data into `sis_db` (department → colleges → users → college_requests → students → courses → enrollments).

## Frontend (Static Web Apps)

**App name:** sis-frontend

**Frontend URL:**

- https://thankful-coast-07aa3a700.6.azurestaticapps.net/

**Build settings:**

- App location: `Frontend`
- Output location: `dist`

**Environment variables (Static Web App → Configuration):**

- VITE_API_BASE_URL = https://sis-backend-server.azurewebsites.net

## Cleanup After Demo

To stop billing after the demo:

1. Delete the App Service.
2. Delete the MySQL Flexible Server.
3. Delete the Static Web App.
4. (Optional) Delete the resource group to remove everything at once.
