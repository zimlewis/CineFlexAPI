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
- **Response:** Registered account object

### Login
- **POST** `/api/authentication/login`
- **Body:**
  ```json
  {
    "email": "string",
    "password": "string"
  }
  ```
- **Response:** JWT token string

### Profile
- **GET** `/api/authentication/profile`
- **Headers:** `Authorization: Bearer <token>`
- **Response:** Account object

---

## Shows

### Get All Shows
- **GET** `/api/shows`
- **Response:** List of shows

### Get Show by ID
- **GET** `/api/shows/{id}`
- **Response:** Show object

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
    "isSeries": true
  }
  ```
- **Response:** Created show object

### Update Show
- **PUT** `/api/shows/{id}`
- **Body:** Same as Add Show
- **Response:** Updated show object

### Delete Show
- **DELETE** `/api/shows/{id}`
- **Response:** 200 OK

### Get Seasons of a Show
- **GET** `/api/shows/{id}/seasons`
- **Response:** List of seasons

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
- **Response:** Created season object

---

## Seasons

### Get Season by ID
- **GET** `/api/seasons/{id}`
- **Response:** Season object

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
- **Response:** Updated season object

### Delete Season
- **DELETE** `/api/seasons/{id}`
- **Response:** 200 OK

### Get Episodes of a Season
- **GET** `/api/seasons/{id}/episodes`
- **Response:** List of episodes

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
- **Response:** Created episode object

---

## Episodes

### Get Episode by ID
- **GET** `/api/episodes/{id}`
- **Response:** Episode object

### Update Episode
- **PUT** `/api/episodes/{id}`
- **Body:** Same as Add Episode
- **Response:** Updated episode object

### Delete Episode
- **DELETE** `/api/episodes/{id}`
- **Response:** 200 OK

---

> For all endpoints requiring authentication, include the `Authorization: Bearer <token>` header.