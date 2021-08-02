# todoapp-clean-arch
Todo app using clean architecture, graph-ql, repository pattern, coroutines and mvvm

## Clean Architecture

![clean arch img](https://www.oncehub.com/hs-fs/hubfs/Marketing/02.%20Website%20assets/03.%20Blog/new-posts/explaining-clean-architecture/The%20Clean%20Architecture.png?width=772&name=The%20Clean%20Architecture.png)

This project follows clean architecture layers. It's divided in two modules: core (abstract, business logic) and app (UI and implementations)

## GraphQL
Search for api.graphl file to check available queries and mutations.

## Api Key
In order to use the app, you should provide a valid API KEY. For this, create a gradle.properties file within app module and set 
API_KEY=<your_api_key>

## Features
You can fetch exiting and add new todos. Add/Remove/Update operations are not yet implemented. For simplicity, the UI is minimalist and the same user is always used.
