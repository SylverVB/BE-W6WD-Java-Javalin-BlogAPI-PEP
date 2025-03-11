# User Stories for Social Media API

## 1. User Registration

**As a user**, I should be able to create a new account on the endpoint `POST localhost:8080/register`. The body will contain a representation of a JSON Account, but will not contain an `account_id`.

- **Registration will be successful** if:
  - The username is not blank.
  - The password is at least 4 characters long.
  - An account with that username does not already exist.
  
- **Response**:
  - If successful: A JSON of the Account, including its `account_id`, with a 200 OK status.
  - If unsuccessful: A 400 status (Client error).

---

## 2. Login

**As a user**, I should be able to verify my login on the endpoint `POST localhost:8080/login`. The request body will contain a JSON representation of an Account, not containing `account_id`.

- **Login will be successful** if:
  - The username and password provided in the request body match a real account in the database.

- **Response**:
  - If successful: A JSON of the account in the response body, including its `account_id`, with a 200 OK status.
  - If unsuccessful: A 401 status (Unauthorized).

---

## 3. Create New Message

**As a user**, I should be able to submit a new post on the endpoint `POST localhost:8080/messages`. The request body will contain a JSON representation of a message, which should be persisted to the database, but will not contain `message_id`.

- **Message creation will be successful** if:
  - `message_text` is not blank and is under 255 characters.
  - `posted_by` refers to a real, existing user.
  
- **Response**:
  - If successful: A JSON of the message, including its `message_id`, with a 200 OK status.
  - If unsuccessful: A 400 status (Client error).

---

## 4. Get All Messages

**As a user**, I should be able to submit a `GET` request on the endpoint `GET localhost:8080/messages`.

- **Response**:
  - A JSON representation of a list containing all messages retrieved from the database.
  - If there are no messages, the list should simply be empty.
  - The response status should always be 200 OK.

---

## 5. Get One Message Given Message Id

**As a user**, I should be able to submit a `GET` request on the endpoint `GET localhost:8080/messages/{message_id}`.

- **Response**:
  - A JSON representation of the message identified by the `message_id`.
  - If the message does not exist, the response body should be empty.
  - The response status should always be 200 OK.

---

## 6. Delete a Message Given Message Id

**As a user**, I should be able to submit a `DELETE` request on the endpoint `DELETE localhost:8080/messages/{message_id}`.

- **Response**:
  - If the message existed: A JSON of the now-deleted message, with a 200 OK status.
  - If the message did not exist: An empty response body with a 200 OK status.
  - The response status should be 200 (idempotent operation).

---

## 7. Update Message Given Message Id

**As a user**, I should be able to submit a `PATCH` request on the endpoint `PATCH localhost:8080/messages/{message_id}`. The request body should contain a new `message_text` to replace the message identified by `message_id`.

- **Message update will be successful** if:
  - The `message_id` already exists.
  - The new `message_text` is not blank and is under 255 characters.

- **Response**:
  - If successful: A JSON of the full updated message (including `message_id`, `posted_by`, `message_text`, and `time_posted_epoch`), with a 200 OK status.
  - If unsuccessful: A 400 status (Client error).

---

## 8. Get All Messages From User Given Account Id

**As a user**, I should be able to submit a `GET` request on the endpoint `GET localhost:8080/accounts/{account_id}/messages`.

- **Response**:
  - A JSON representation of a list containing all messages posted by a particular user, retrieved from the database.
  - If the user has no messages, the list should simply be empty.
  - The response status should always be 200 OK.