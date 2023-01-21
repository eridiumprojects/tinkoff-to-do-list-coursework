# Tinkoff To-Do-List REST API coursework

## User

#### Register a new user

`POST /register`

__Request body__:
```
{
   "login": "<string>",
   "password": "<string>",
   "email": "<string>"
}
```

__Response body__:
```
{
   "login": "<string>",
   "password": "<string>",
   "email": "<string>"
}
```

## Task

#### Create a task

`POST /create`

__Request body__:
```
{
   "data": "<string>"
}
```

__Response body__:
```
{
   "id": "<Long>",
   "data": "<string>,
   "checkbox": <boolean>,
   "deadline": <string>,
   "created": <string>,
   "modified": <string>
}
```

#### Get a task by Id

`GET /task/{{id}}`

__Response body__:
```
{
   "id": "<Long>",
   "data": "<string>,
   "checkbox": <boolean>,
   "deadline": <string>,
   "created": <string>,
   "modified": <string>
}
```

#### Update a task by Id

`PUT /update/{{id}}`

__Request body__:
```
{
   "data": "<string>"
}
```

__Response body__:
```
{
   "id": "<Long>",
   "data": "<string>,
   "checkbox": <boolean>,
   "deadline": <string>,
   "created": <string>,
   "modified": <string>
}
```

#### Delete a task by Id

`DELETE /delete/{{id}}`

__Response body__:
```
HTTP/1.1 200 
```

#### Get all tasks

`GET /tasks`

__Response body__:
```
[
  {
   "id": "<Long>",
   "data": "<string>,
   "checkbox": <boolean>,
   "deadline": <string>,
   "created": <string>,
   "modified": <string>
  },
  {
   "id": "<Long>",
   "data": "<string>,
   "checkbox": <boolean>,
   "deadline": <string>,
   "created": <string>,
   "modified": <string>
  }
  ...
]
```

#### Mark a task as completed

`PUT /task/notice/{{id}}`

__Response body__:
```
{
   "id": "<Long>",
   "data": "<string>,
   "checkbox": <boolean>,
   "deadline": <string>,
   "created": <string>,
   "modified": <string>
}
```
#### Set task deadline

`PUT /task/deadline/{{id}}`

__Request body__:
```
{
  "day": "<string>",
  "month": "<string>",
  "year": "<int>",
  "hours": "<string>",
  "seconds": "<string>"
}
```

__Response body__:
```
{
   "id": "<Long>",
   "data": "<string>,
   "checkbox": <boolean>,
   "deadline": <string>,
   "created": <string>,
   "modified": <string>
}
```

#### Tasks search and order

#### `GET /search?data=DATA`    //Will find a task with the name {DATA}
#### `GET /search?actual=0`    //Will find completed tasks ordered by deadline 
#### `GET /search?actual=1`    //Will find uncompleted tasks ordered by deadline

#### All of the task we can ordered by created time
#### For example, we need to find all completed tasks ordered by created time, our request should look like this:
`GET /search?data=Do+Java&ordered_by=created_time`

__Response body__:
```
[
  {
    "id": 6,
    "data": "Do Java",
    "checkbox": true,
    "deadline": null,
    "created": "19-01-2023_13:38:43",
    "modified": "19-01-2023_13:40:40"
  },
  {
    "id": 10,
    "data": "Do Java",
    "checkbox": true,
    "deadline": "04-05-2022_22:56",
    "created": "20-01-2023_11:07",
    "modified": "21-01-2023_13:36"
  }
]
```

## UML-Diagram of project
![UML_TODO](https://github.com/pestrikv/to-do-list-coursework/blob/master/UML_to-do-coursework.jpg)





