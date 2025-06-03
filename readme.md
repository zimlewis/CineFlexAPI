# CineFlex API Documentation

## Authentication

### Register
- **POST** `/api/authentication/register`
- **Body:**
  ```json
  {
    "username": "string",
    "password": "string",
    "email": "string"
  }
  ```
- **Response:**
  ```json
  {
    "id": "uuid",
    "username": "string",
    "email": "string",
    "createdAt": "YYYY-MM-DDTHH:MM:SSZ",
    "verify": false
  }
  ```

### Login
- **POST** `/api/authentication/login`
- **Body:**
  ```json
  {
    "email": "string",
    "password": "string"
  }
  ```
- **Response:**
  ```json
  "jwt-token-string"
  ```

### Profile
- **GET** `/api/authentication/profile`
- **Headers:** `Authorization: Bearer <token>`
- **Response:**
  ```json
  {
    "id": "uuid",
    "username": "string",
    "email": "string",
    "createdAt": "YYYY-MM-DDTHH:MM:SSZ",
    "verify": false
  }
  ```

### Send Verification Email
- **POST** `/api/authentication/verify`
- **Body:**
  ```json
  {
    "email": "string"
  }
  ```
- **Response:**
  - `204 OK`
  - Error responses:
    - `406 NOT_ACCEPTABLE` if email is null
    - `401 UNAUTHORIZED` if email not found
    - `409 CONFLICT` if account already verified

### Verify Account
- **GET** `/api/authentication/verify/{token}`
- **Response:**
  - `204 NO_CONTENT` if successful
  - Error responses with appropriate status and message if token is invalid or expired

---

## Shows

### Get All Shows
- **GET** `/api/shows`
- **Response:**
  ```json
  [
    {
      "id": "uuid",
      "title": "string",
      "description": "string",
      "releaseDate": "YYYY-MM-DD",
      "thumbnail": "string",
      "onGoing": true,
      "isSeries": true,
      "ageRating": "string"
    }
  ]
  ```

### Get Show by ID
- **GET** `/api/shows/{id}`
- **Response:**
  ```json
  {
    "id": "uuid",
    "title": "string",
    "description": "string",
    "releaseDate": "YYYY-MM-DD",
    "thumbnail": "string",
    "onGoing": true,
    "isSeries": true,
    "ageRating": "string"
  }
  ```

### Add Show
- **POST** `/api/shows`
- **Authority Required:** `ADD_CONTENT`
- **Body:**
  ```json
  {
    "title": "string",
    "description": "string",
    "releaseDate": "YYYY-MM-DD",
    "thumbnail": "string",
    "onGoing": true,
    "isSeries": true,
    "ageRating": "string"
  }
  ```
- **Response:**
  ```json
  {
    "id": "uuid",
    "title": "string",
    "description": "string",
    "releaseDate": "YYYY-MM-DD",
    "thumbnail": "string",
    "onGoing": true,
    "isSeries": true,
    "ageRating": "string"
  }
  ```

### Update Show
- **PUT** `/api/shows/{id}`
- **Authority Required:** `EDIT_CONTENT`
- **Body:**
  ```json
  {
    "title": "string",
    "description": "string",
    "releaseDate": "YYYY-MM-DD",
    "thumbnail": "string",
    "onGoing": true,
    "isSeries": true,
    "ageRating": "string"
  }
  ```
- **Response:**
  ```json
  {
    "id": "uuid",
    "title": "string",
    "description": "string",
    "releaseDate": "YYYY-MM-DD",
    "thumbnail": "string",
    "onGoing": true,
    "isSeries": true,
    "ageRating": "string"
  }
  ```

### Delete Show
- **DELETE** `/api/shows/{id}`
- **Authority Required:** `DELETE_CONTENT`
- **Response:**
  ```json
  {
    "message": "Show deleted successfully"
  }
  ```

### Get Seasons of a Show
- **GET** `/api/shows/{id}/seasons`
- **Response:**
  ```json
  [
    {
      "id": "uuid",
      "title": "string",
      "releaseDate": "YYYY-MM-DD",
      "description": "string",
      "show": "uuid"
    }
  ]
  ```

### Add Season to Show
- **POST** `/api/shows/{id}/seasons`
- **Authority Required:** `ADD_CONTENT`
- **Body:**
  ```json
  {
    "title": "string",
    "releaseDate": "YYYY-MM-DD",
    "description": "string"
  }
  ```
- **Response:**
  ```json
  {
    "id": "uuid",
    "title": "string",
    "releaseDate": "YYYY-MM-DD",
    "description": "string",
    "show": "uuid"
  }
  ```

---

## Seasons

### Get Season by ID
- **GET** `/api/seasons/{id}`
- **Response:**
  ```json
  {
    "id": "uuid",
    "title": "string",
    "releaseDate": "YYYY-MM-DD",
    "description": "string",
    "show": "uuid"
  }
  ```

### Update Season
- **PUT** `/api/seasons/{id}`
- **Authority Required:** `EDIT_CONTENT`
- **Body:**
  ```json
  {
    "title": "string",
    "releaseDate": "YYYY-MM-DD",
    "description": "string",
    "show": "uuid (optional)"
  }
  ```
- **Response:**
  ```json
  {
    "id": "uuid",
    "title": "string",
    "releaseDate": "YYYY-MM-DD",
    "description": "string",
    "show": "uuid"
  }
  ```

### Delete Season
- **DELETE** `/api/seasons/{id}`
- **Authority Required:** `DELETE_CONTENT`
- **Response:**
  ```json
  {
    "message": "Season deleted successfully"
  }
  ```

### Get Episodes of a Season
- **GET** `/api/seasons/{id}/episodes`
- **Response:**
  ```json
  [
    {
      "id": "uuid",
      "title": "string",
      "number": "string",
      "description": "string",
      "url": "string",
      "releaseDate": "YYYY-MM-DD",
      "duration": 0,
      "openingStart": 0,
      "openingEnd": 0,
      "season": "uuid"
    }
  ]
  ```

### Add Episode to Season
- **POST** `/api/seasons/{id}/episodes`
- **Authority Required:** `ADD_CONTENT`
- **Body:**
  ```json
  {
    "title": "string",
    "number": "string",
    "description": "string",
    "url": "string",
    "releaseDate": "YYYY-MM-DD",
    "duration": 0,
    "openingStart": 0,
    "openingEnd": 0
  }
  ```
- **Response:**
  ```json
  {
    "id": "uuid",
    "title": "string",
    "number": "string",
    "description": "string",
    "url": "string",
    "releaseDate": "YYYY-MM-DD",
    "duration": 0,
    "openingStart": 0,
    "openingEnd": 0,
    "season": "uuid"
  }
  ```

---

## Episodes

### Get Episode by ID
- **GET** `/api/episodes/{id}`
- **Response:**
  ```json
  {
    "id": "uuid",
    "title": "string",
    "number": "string",
    "description": "string",
    "url": "string",
    "releaseDate": "YYYY-MM-DD",
    "duration": 0,
    "openingStart": 0,
    "openingEnd": 0,
    "season": "uuid"
  }
  ```

### Update Episode
- **PUT** `/api/episodes/{id}`
- **Authority Required:** `EDIT_CONTENT`
- **Body:**
  ```json
  {
    "title": "string",
    "number": "string",
    "description": "string",
    "url": "string",
    "releaseDate": "YYYY-MM-DD",
    "duration": 0,
    "openingStart": 0,
    "openingEnd": 0
  }
  ```
- **Response:**
  ```json
  {
    "id": "uuid",
    "title": "string",
    "number": "string",
    "description": "string",
    "url": "string",
    "releaseDate": "YYYY-MM-DD",
    "duration": 0,
    "openingStart": 0,
    "openingEnd": 0,
    "season": "uuid"
  }
  ```

### Delete Episode
- **DELETE** `/api/episodes/{id}`
- **Authority Required:** `DELETE_CONTENT`
- **Response:**
  ```json
  {
    "message": "Episode deleted successfully"
  }
  ```

---

> For all endpoints requiring authentication, include the `Authorization: Bearer <token>` header.

