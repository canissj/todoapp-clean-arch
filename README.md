# todoapp-clean-arch
TO-DO android (Kotlin) app using:
* Clean architecture
* GraphQL 
* Repository pattern
* Coroutines
* MVVM

## Clean Architecture

![clean arch img](https://www.oncehub.com/hs-fs/hubfs/Marketing/02.%20Website%20assets/03.%20Blog/new-posts/explaining-clean-architecture/The%20Clean%20Architecture.png?width=772&name=The%20Clean%20Architecture.png)

This project follows clean architecture layers. It's divided into two modules: 
1. `core` (abstract, business logic) 
2. `app` (UI and implementations)

## GraphQL
Search for `api.graphl` file to check available queries and mutations.

## Installation

### API Key
In order to use the app, you should provide a valid API Key. In order to do this (pun intended), create a gradle.properties file within the `app` module and set 
API_KEY=<your_api_key>

## Features
* Fetch existing and add new TO-DOs
* Remove/Update operations are not yet implemented
* For simplicity, the UI is minimalist and the same user is always used
