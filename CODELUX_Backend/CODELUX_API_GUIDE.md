# CODELUX Backend API Guide

Base URL while running locally:

```text
http://localhost:8080
```

## Database

The app uses MySQL by default and creates/updates tables with Hibernate.

```properties
DB_URL=jdbc:mysql://localhost:3306/codelux_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=
```

## Cloudinary Image Upload

Set these values before starting Spring Boot. Uploaded admin images will go to Cloudinary and the saved URL will be the Cloudinary `secure_url`.

```properties
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret
CLOUDINARY_ROOT_FOLDER=codelux
```

If these Cloudinary values are empty, uploads fall back to local `/uploads/...` files.

## First Admin Setup

The backend creates one default admin automatically when the `admin_users` table is empty:

```text
username: admin
email: rupnar8459@gmail.com
password: admin123
role: SUPER_ADMIN
```

You can change it before startup:

```properties
ADMIN_INITIALIZER_ENABLED=true
ADMIN_INITIALIZER_EMAIL=rupnar8459@gmail.com
ADMIN_INITIALIZER_USERNAME=admin
ADMIN_INITIALIZER_FULL_NAME=Super Admin
ADMIN_INITIALIZER_PASSWORD=admin123
```

The initializer creates only one admin. If any admin already exists, it skips creation.

The response includes a token. Send it on admin requests:

```http
Authorization: Bearer YOUR_TOKEN
```

Admin tokens are signed stateless tokens. They are stored by the browser/frontend and are not saved in an `admin_sessions` database table.

## Admin Auth

```http
POST /api/admin/auth/login
GET  /api/admin/auth/me
```

Login body:

```json
{
  "email": "rupnar8459@gmail.com",
  "password": "admin123"
}
```

## Admin Dashboard APIs

All routes below require the bearer token.

```http
GET    /api/admin/settings
PUT    /api/admin/settings

GET    /api/admin/media
POST   /api/admin/media
PUT    /api/admin/media/{id}
DELETE /api/admin/media/{id}

GET    /api/admin/jobs
POST   /api/admin/jobs
PUT    /api/admin/jobs/{id}
DELETE /api/admin/jobs/{id}

GET    /api/admin/team
POST   /api/admin/team
PUT    /api/admin/team/{id}
DELETE /api/admin/team/{id}

GET    /api/admin/contacts
PATCH  /api/admin/contacts/{id}/status
DELETE /api/admin/contacts/{id}

GET    /api/admin/admins
POST   /api/admin/admins
PUT    /api/admin/admins/{id}
DELETE /api/admin/admins/{id}

POST   /api/admin/uploads
```

Upload images with multipart form fields:

```text
file=<image file>
folder=slider
```

Use folders such as `slider`, `theme`, `team`, `owner`, or `settings`.

## Public Frontend APIs

Use these from the animated website/frontend:

```http
GET  /api/public/settings
GET  /api/public/media
GET  /api/public/media?type=HERO_SLIDE
GET  /api/public/media?type=THEME_IMAGE
GET  /api/public/jobs
GET  /api/public/team
POST /api/public/contacts
```

Media types:

```text
HERO_SLIDE
THEME_IMAGE
GALLERY
LOGO
BACKGROUND
```

Contact form body:

```json
{
  "fullName": "Customer Name",
  "email": "customer@example.com",
  "phone": "+91 9999999999",
  "subject": "Project inquiry",
  "message": "I want to contact the company."
}
```

Contact statuses:

```text
NEW
READ
REPLIED
ARCHIVED
```
