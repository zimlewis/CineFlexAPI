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
    "createdAt": "YYYY-MM-DDTHH:MM:SSZ"
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
  {
    "token": "jwt-token-string"
  }
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
    "createdAt": "YYYY-MM-DDTHH:MM:SSZ"
  }
  ```

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
      "isSeries": true
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
    "isSeries": true
  }
  ```

### Add Show
- **POST** `/api/shows`
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
- **Response:**
  ```json
  {
    "message": "Episode deleted successfully"
  }
  ```

---

> For all endpoints requiring authentication, include the `Authorization: Bearer <token>` header.