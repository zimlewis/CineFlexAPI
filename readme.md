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
  "jwt-token-string"
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
- **Headers:** `host: <your-domain>`
- **Body:**
  ```json
  {
    "email": "string"
  }
  ```
- **Response:**
  - `204 NO_CONTENT` if successful
  - Error responses:
    - `406 NOT_ACCEPTABLE` if email is null
    - `401 UNAUTHORIZED` if email not found
    - `409 CONFLICT` if account already verified

### Verify Account
- **GET** `/api/authentication/verify/{token}`
- **Response:**
  - `204 NO_CONTENT` if successful
  - Error responses with appropriate status and message if token is invalid or expired

### Get User Role
- **GET** `/api/authentication/role`
- **Headers:** `Authorization: Bearer <token>`
- **Response:**
  ```json
  0
  ```
  (or another integer representing the user's role)

---

## Users

### Get User by ID
- **GET** `/api/users/{id}`
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

### Get Current User Subscription
- **GET** `/api/users/subscription`
- **Headers:** `Authorization: Bearer <token>`
- **Response:**
  ```json
  {
    "id": "uuid",
    "account": "uuid",
    "subscriptionType": "string",
    "startDate": "YYYY-MM-DDTHH:MM:SSZ",
    "endDate": "YYYY-MM-DDTHH:MM:SSZ",
    "active": true
  }
  ```
  - Returns `204 NO_CONTENT` if the user has no subscription.

---

## Orders

### Create Order
- **POST** `/api/orders`
- **Headers:** `Authorization: Bearer <token>`
- **Response:**
  ```json
  {
    "id": "uuid",
    "account": "uuid",
    "subscription": "uuid",
    "amount": 10000.0,
    "createdTime": "YYYY-MM-DDTHH:MM:SSZ",
    "paidTime": null,
    "paid": false
  }
  ```

### Get Order by ID
- **GET** `/api/orders/{id}`
- **Headers:** `Authorization: Bearer <token>`
- **Response:**
  ```json
  {
    "id": "uuid",
    "account": "uuid",
    "subscription": "uuid",
    "amount": 10000.0,
    "createdTime": "YYYY-MM-DDTHH:MM:SSZ",
    "paidTime": null,
    "paid": false
  }
  ```

### Confirm Order (Webhook Only)
- **POST** `/api/orders/confirm`
- **Note:** This endpoint is for payment provider webhook callbacks only.  
  Do **not** call this directly from the client.
- **Body:**
  ```json
  {
    "transferType": "string",
    "content": "string",
    "referenceCode": "string",
    "transferAmount": 10000.0
  }
  ```
- **Response:**
  - `200 OK` if successful

---

## Health Check

### Ping
- **GET** `/api/health/ping`
- **Response:**  
  ```
  pong
  ```

---

## Genres

### Add Genre
- **POST** `/api/genres`
- **Authority Required:** `ADD_CONTENT`
- **Body:**
  ```json
  {
    "name": "string"
  }
  ```
- **Response:**
  ```json
  {
    "id": "uuid",
    "name": "string"
  }
  ```

### Get Genre by ID
- **GET** `/api/genres/{id}`
- **Response:**
  ```json
  {
    "id": "uuid",
    "name": "string"
  }
  ```

---

## Shows

### Get All Shows
- **GET** `/api/shows`
- **Query Parameters:**
  - `genres`: (optional) List of genre names. Example: `/api/shows?genres=Action&genres=Drama`
- **Note:**  
  If the `genres` parameter is provided, this endpoint returns all shows that have **all** the provided genres.
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
  - `200 OK` if successful

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

### Get Genres of a Show
- **GET** `/api/shows/{id}/genres`
- **Response:**
  ```json
  [
    {
      "id": "uuid",
      "name": "string"
    }
  ]
  ```

### Add Genres to Show
- **POST** `/api/shows/{id}/genres`
- **Authority Required:** `EDIT_CONTENT`
- **Body:**
  ```json
  {
    "genres": [
      "uuid",
      "uuid"
    ]
  }
  ```
- **Response:**
  ```json
  [
    {
      "id": "uuid",
      "name": "string"
    }
  ]
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
  - `200 OK` if successful

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
  - `200 OK` if successful

### Get Comments of an Episode
- **GET** `/api/episodes/{id}/comments`
- **Response:**
  ```json
  [
    {
      "id": "uuid",
      "content": "string",
      "account": "uuid",
      "episode": "uuid",
      "createdAt": "YYYY-MM-DDTHH:MM:SSZ"
    }
  ]
  ```

---

## Comments

### Add Comment to an Episode
- **POST** `/api/comments/{id}`
- **Headers:** `Authorization: Bearer <token>`
- **Body:**
  ```json
  {
    "content": "string"
  }
  ```
- **Response:**
  ```json
  {
    "id": "uuid",
    "content": "string",
    "account": "uuid",
    "episode": "uuid",
    "createdAt": "YYYY-MM-DDTHH:MM:SSZ"
  }
  ```

---

> For all endpoints requiring authentication, include the `Authorization: Bearer <token>` header.
> 
> **Note:** `/api/orders/confirm` is for payment provider webhook callbacks only. Do **not** call this directly from the client.

