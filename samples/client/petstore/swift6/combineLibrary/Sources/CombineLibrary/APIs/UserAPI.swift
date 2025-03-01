//
// UserAPI.swift
//
// Generated by openapi-generator
// https://openapi-generator.tech
//

import Foundation
#if canImport(Combine)
import Combine
#endif

open class UserAPI {

    /**
     Create user
     
     - parameter body: (body) Created user object 
     - parameter openAPIClient: The OpenAPIClient that contains the configuration for the http request.
     - returns: AnyPublisher<Void, Error>
     */
    #if canImport(Combine)
    @available(macOS 10.15, iOS 13.0, tvOS 13.0, watchOS 6.0, *)
    open class func createUser(body: User, openAPIClient: OpenAPIClient = OpenAPIClient.shared) -> AnyPublisher<Void, Error> {
        let requestBuilder = createUserWithRequestBuilder(body: body, openAPIClient: openAPIClient)
        let requestTask = requestBuilder.requestTask
        return Future<Void, Error> { promise in
            nonisolated(unsafe) let promise = promise
            requestBuilder.execute { result in
                switch result {
                case .success:
                    promise(.success(()))
                case let .failure(error):
                    promise(.failure(error))
                }
            }
        }
        .handleEvents(receiveCancel: {
            requestTask.cancel()
        })
        .eraseToAnyPublisher()
    }
    #endif

    /**
     Create user
     - POST /user
     - This can only be done by the logged in user.
     - parameter body: (body) Created user object 
     - parameter openAPIClient: The OpenAPIClient that contains the configuration for the http request.
     - returns: RequestBuilder<Void> 
     */
    open class func createUserWithRequestBuilder(body: User, openAPIClient: OpenAPIClient = OpenAPIClient.shared) -> RequestBuilder<Void> {
        let localVariablePath = "/user"
        let localVariableURLString = openAPIClient.basePath + localVariablePath
        let localVariableParameters = JSONEncodingHelper.encodingParameters(forEncodableObject: body, codableHelper: openAPIClient.codableHelper)

        let localVariableUrlComponents = URLComponents(string: localVariableURLString)

        let localVariableNillableHeaders: [String: Any?] = [
            :
        ]

        let localVariableHeaderParameters = APIHelper.rejectNilHeaders(localVariableNillableHeaders)

        let localVariableRequestBuilder: RequestBuilder<Void>.Type = openAPIClient.requestBuilderFactory.getNonDecodableBuilder()

        return localVariableRequestBuilder.init(method: "POST", URLString: (localVariableUrlComponents?.string ?? localVariableURLString), parameters: localVariableParameters, headers: localVariableHeaderParameters, requiresAuthentication: false, openAPIClient: openAPIClient)
    }

    /**
     Creates list of users with given input array
     
     - parameter body: (body) List of user object 
     - parameter openAPIClient: The OpenAPIClient that contains the configuration for the http request.
     - returns: AnyPublisher<Void, Error>
     */
    #if canImport(Combine)
    @available(macOS 10.15, iOS 13.0, tvOS 13.0, watchOS 6.0, *)
    open class func createUsersWithArrayInput(body: [User], openAPIClient: OpenAPIClient = OpenAPIClient.shared) -> AnyPublisher<Void, Error> {
        let requestBuilder = createUsersWithArrayInputWithRequestBuilder(body: body, openAPIClient: openAPIClient)
        let requestTask = requestBuilder.requestTask
        return Future<Void, Error> { promise in
            nonisolated(unsafe) let promise = promise
            requestBuilder.execute { result in
                switch result {
                case .success:
                    promise(.success(()))
                case let .failure(error):
                    promise(.failure(error))
                }
            }
        }
        .handleEvents(receiveCancel: {
            requestTask.cancel()
        })
        .eraseToAnyPublisher()
    }
    #endif

    /**
     Creates list of users with given input array
     - POST /user/createWithArray
     - parameter body: (body) List of user object 
     - parameter openAPIClient: The OpenAPIClient that contains the configuration for the http request.
     - returns: RequestBuilder<Void> 
     */
    open class func createUsersWithArrayInputWithRequestBuilder(body: [User], openAPIClient: OpenAPIClient = OpenAPIClient.shared) -> RequestBuilder<Void> {
        let localVariablePath = "/user/createWithArray"
        let localVariableURLString = openAPIClient.basePath + localVariablePath
        let localVariableParameters = JSONEncodingHelper.encodingParameters(forEncodableObject: body, codableHelper: openAPIClient.codableHelper)

        let localVariableUrlComponents = URLComponents(string: localVariableURLString)

        let localVariableNillableHeaders: [String: Any?] = [
            :
        ]

        let localVariableHeaderParameters = APIHelper.rejectNilHeaders(localVariableNillableHeaders)

        let localVariableRequestBuilder: RequestBuilder<Void>.Type = openAPIClient.requestBuilderFactory.getNonDecodableBuilder()

        return localVariableRequestBuilder.init(method: "POST", URLString: (localVariableUrlComponents?.string ?? localVariableURLString), parameters: localVariableParameters, headers: localVariableHeaderParameters, requiresAuthentication: false, openAPIClient: openAPIClient)
    }

    /**
     Creates list of users with given input array
     
     - parameter body: (body) List of user object 
     - parameter openAPIClient: The OpenAPIClient that contains the configuration for the http request.
     - returns: AnyPublisher<Void, Error>
     */
    #if canImport(Combine)
    @available(macOS 10.15, iOS 13.0, tvOS 13.0, watchOS 6.0, *)
    open class func createUsersWithListInput(body: [User], openAPIClient: OpenAPIClient = OpenAPIClient.shared) -> AnyPublisher<Void, Error> {
        let requestBuilder = createUsersWithListInputWithRequestBuilder(body: body, openAPIClient: openAPIClient)
        let requestTask = requestBuilder.requestTask
        return Future<Void, Error> { promise in
            nonisolated(unsafe) let promise = promise
            requestBuilder.execute { result in
                switch result {
                case .success:
                    promise(.success(()))
                case let .failure(error):
                    promise(.failure(error))
                }
            }
        }
        .handleEvents(receiveCancel: {
            requestTask.cancel()
        })
        .eraseToAnyPublisher()
    }
    #endif

    /**
     Creates list of users with given input array
     - POST /user/createWithList
     - parameter body: (body) List of user object 
     - parameter openAPIClient: The OpenAPIClient that contains the configuration for the http request.
     - returns: RequestBuilder<Void> 
     */
    open class func createUsersWithListInputWithRequestBuilder(body: [User], openAPIClient: OpenAPIClient = OpenAPIClient.shared) -> RequestBuilder<Void> {
        let localVariablePath = "/user/createWithList"
        let localVariableURLString = openAPIClient.basePath + localVariablePath
        let localVariableParameters = JSONEncodingHelper.encodingParameters(forEncodableObject: body, codableHelper: openAPIClient.codableHelper)

        let localVariableUrlComponents = URLComponents(string: localVariableURLString)

        let localVariableNillableHeaders: [String: Any?] = [
            :
        ]

        let localVariableHeaderParameters = APIHelper.rejectNilHeaders(localVariableNillableHeaders)

        let localVariableRequestBuilder: RequestBuilder<Void>.Type = openAPIClient.requestBuilderFactory.getNonDecodableBuilder()

        return localVariableRequestBuilder.init(method: "POST", URLString: (localVariableUrlComponents?.string ?? localVariableURLString), parameters: localVariableParameters, headers: localVariableHeaderParameters, requiresAuthentication: false, openAPIClient: openAPIClient)
    }

    /**
     Delete user
     
     - parameter username: (path) The name that needs to be deleted 
     - parameter openAPIClient: The OpenAPIClient that contains the configuration for the http request.
     - returns: AnyPublisher<Void, Error>
     */
    #if canImport(Combine)
    @available(macOS 10.15, iOS 13.0, tvOS 13.0, watchOS 6.0, *)
    open class func deleteUser(username: String, openAPIClient: OpenAPIClient = OpenAPIClient.shared) -> AnyPublisher<Void, Error> {
        let requestBuilder = deleteUserWithRequestBuilder(username: username, openAPIClient: openAPIClient)
        let requestTask = requestBuilder.requestTask
        return Future<Void, Error> { promise in
            nonisolated(unsafe) let promise = promise
            requestBuilder.execute { result in
                switch result {
                case .success:
                    promise(.success(()))
                case let .failure(error):
                    promise(.failure(error))
                }
            }
        }
        .handleEvents(receiveCancel: {
            requestTask.cancel()
        })
        .eraseToAnyPublisher()
    }
    #endif

    /**
     Delete user
     - DELETE /user/{username}
     - This can only be done by the logged in user.
     - parameter username: (path) The name that needs to be deleted 
     - parameter openAPIClient: The OpenAPIClient that contains the configuration for the http request.
     - returns: RequestBuilder<Void> 
     */
    open class func deleteUserWithRequestBuilder(username: String, openAPIClient: OpenAPIClient = OpenAPIClient.shared) -> RequestBuilder<Void> {
        var localVariablePath = "/user/{username}"
        let usernamePreEscape = "\(APIHelper.mapValueToPathItem(username))"
        let usernamePostEscape = usernamePreEscape.addingPercentEncoding(withAllowedCharacters: .urlPathAllowed) ?? ""
        localVariablePath = localVariablePath.replacingOccurrences(of: "{username}", with: usernamePostEscape, options: .literal, range: nil)
        let localVariableURLString = openAPIClient.basePath + localVariablePath
        let localVariableParameters: [String: Any]? = nil

        let localVariableUrlComponents = URLComponents(string: localVariableURLString)

        let localVariableNillableHeaders: [String: Any?] = [
            :
        ]

        let localVariableHeaderParameters = APIHelper.rejectNilHeaders(localVariableNillableHeaders)

        let localVariableRequestBuilder: RequestBuilder<Void>.Type = openAPIClient.requestBuilderFactory.getNonDecodableBuilder()

        return localVariableRequestBuilder.init(method: "DELETE", URLString: (localVariableUrlComponents?.string ?? localVariableURLString), parameters: localVariableParameters, headers: localVariableHeaderParameters, requiresAuthentication: false, openAPIClient: openAPIClient)
    }

    /**
     Get user by user name
     
     - parameter username: (path) The name that needs to be fetched. Use user1 for testing. 
     - parameter openAPIClient: The OpenAPIClient that contains the configuration for the http request.
     - returns: AnyPublisher<User, Error>
     */
    #if canImport(Combine)
    @available(macOS 10.15, iOS 13.0, tvOS 13.0, watchOS 6.0, *)
    open class func getUserByName(username: String, openAPIClient: OpenAPIClient = OpenAPIClient.shared) -> AnyPublisher<User, Error> {
        let requestBuilder = getUserByNameWithRequestBuilder(username: username, openAPIClient: openAPIClient)
        let requestTask = requestBuilder.requestTask
        return Future<User, Error> { promise in
            nonisolated(unsafe) let promise = promise
            requestBuilder.execute { result in
                switch result {
                case let .success(response):
                    promise(.success(response.body))
                case let .failure(error):
                    promise(.failure(error))
                }
            }
        }
        .handleEvents(receiveCancel: {
            requestTask.cancel()
        })
        .eraseToAnyPublisher()
    }
    #endif

    /**
     Get user by user name
     - GET /user/{username}
     - parameter username: (path) The name that needs to be fetched. Use user1 for testing. 
     - parameter openAPIClient: The OpenAPIClient that contains the configuration for the http request.
     - returns: RequestBuilder<User> 
     */
    open class func getUserByNameWithRequestBuilder(username: String, openAPIClient: OpenAPIClient = OpenAPIClient.shared) -> RequestBuilder<User> {
        var localVariablePath = "/user/{username}"
        let usernamePreEscape = "\(APIHelper.mapValueToPathItem(username))"
        let usernamePostEscape = usernamePreEscape.addingPercentEncoding(withAllowedCharacters: .urlPathAllowed) ?? ""
        localVariablePath = localVariablePath.replacingOccurrences(of: "{username}", with: usernamePostEscape, options: .literal, range: nil)
        let localVariableURLString = openAPIClient.basePath + localVariablePath
        let localVariableParameters: [String: Any]? = nil

        let localVariableUrlComponents = URLComponents(string: localVariableURLString)

        let localVariableNillableHeaders: [String: Any?] = [
            :
        ]

        let localVariableHeaderParameters = APIHelper.rejectNilHeaders(localVariableNillableHeaders)

        let localVariableRequestBuilder: RequestBuilder<User>.Type = openAPIClient.requestBuilderFactory.getBuilder()

        return localVariableRequestBuilder.init(method: "GET", URLString: (localVariableUrlComponents?.string ?? localVariableURLString), parameters: localVariableParameters, headers: localVariableHeaderParameters, requiresAuthentication: false, openAPIClient: openAPIClient)
    }

    /**
     Logs user into the system
     
     - parameter username: (query) The user name for login 
     - parameter password: (query) The password for login in clear text 
     - parameter openAPIClient: The OpenAPIClient that contains the configuration for the http request.
     - returns: AnyPublisher<String, Error>
     */
    #if canImport(Combine)
    @available(macOS 10.15, iOS 13.0, tvOS 13.0, watchOS 6.0, *)
    open class func loginUser(username: String, password: String, openAPIClient: OpenAPIClient = OpenAPIClient.shared) -> AnyPublisher<String, Error> {
        let requestBuilder = loginUserWithRequestBuilder(username: username, password: password, openAPIClient: openAPIClient)
        let requestTask = requestBuilder.requestTask
        return Future<String, Error> { promise in
            nonisolated(unsafe) let promise = promise
            requestBuilder.execute { result in
                switch result {
                case let .success(response):
                    promise(.success(response.body))
                case let .failure(error):
                    promise(.failure(error))
                }
            }
        }
        .handleEvents(receiveCancel: {
            requestTask.cancel()
        })
        .eraseToAnyPublisher()
    }
    #endif

    /**
     Logs user into the system
     - GET /user/login
     - responseHeaders: [X-Rate-Limit(Int), X-Expires-After(Date)]
     - parameter username: (query) The user name for login 
     - parameter password: (query) The password for login in clear text 
     - parameter openAPIClient: The OpenAPIClient that contains the configuration for the http request.
     - returns: RequestBuilder<String> 
     */
    open class func loginUserWithRequestBuilder(username: String, password: String, openAPIClient: OpenAPIClient = OpenAPIClient.shared) -> RequestBuilder<String> {
        let localVariablePath = "/user/login"
        let localVariableURLString = openAPIClient.basePath + localVariablePath
        let localVariableParameters: [String: Any]? = nil

        var localVariableUrlComponents = URLComponents(string: localVariableURLString)
        localVariableUrlComponents?.queryItems = APIHelper.mapValuesToQueryItems([
            "username": (wrappedValue: username.encodeToJSON(codableHelper: openAPIClient.codableHelper), isExplode: false),
            "password": (wrappedValue: password.encodeToJSON(codableHelper: openAPIClient.codableHelper), isExplode: false),
        ])

        let localVariableNillableHeaders: [String: Any?] = [
            :
        ]

        let localVariableHeaderParameters = APIHelper.rejectNilHeaders(localVariableNillableHeaders)

        let localVariableRequestBuilder: RequestBuilder<String>.Type = openAPIClient.requestBuilderFactory.getBuilder()

        return localVariableRequestBuilder.init(method: "GET", URLString: (localVariableUrlComponents?.string ?? localVariableURLString), parameters: localVariableParameters, headers: localVariableHeaderParameters, requiresAuthentication: false, openAPIClient: openAPIClient)
    }

    /**
     Logs out current logged in user session
     
     - parameter openAPIClient: The OpenAPIClient that contains the configuration for the http request.
     - returns: AnyPublisher<Void, Error>
     */
    #if canImport(Combine)
    @available(macOS 10.15, iOS 13.0, tvOS 13.0, watchOS 6.0, *)
    open class func logoutUser(openAPIClient: OpenAPIClient = OpenAPIClient.shared) -> AnyPublisher<Void, Error> {
        let requestBuilder = logoutUserWithRequestBuilder(openAPIClient: openAPIClient)
        let requestTask = requestBuilder.requestTask
        return Future<Void, Error> { promise in
            nonisolated(unsafe) let promise = promise
            requestBuilder.execute { result in
                switch result {
                case .success:
                    promise(.success(()))
                case let .failure(error):
                    promise(.failure(error))
                }
            }
        }
        .handleEvents(receiveCancel: {
            requestTask.cancel()
        })
        .eraseToAnyPublisher()
    }
    #endif

    /**
     Logs out current logged in user session
     - GET /user/logout
     - parameter openAPIClient: The OpenAPIClient that contains the configuration for the http request.
     - returns: RequestBuilder<Void> 
     */
    open class func logoutUserWithRequestBuilder(openAPIClient: OpenAPIClient = OpenAPIClient.shared) -> RequestBuilder<Void> {
        let localVariablePath = "/user/logout"
        let localVariableURLString = openAPIClient.basePath + localVariablePath
        let localVariableParameters: [String: Any]? = nil

        let localVariableUrlComponents = URLComponents(string: localVariableURLString)

        let localVariableNillableHeaders: [String: Any?] = [
            :
        ]

        let localVariableHeaderParameters = APIHelper.rejectNilHeaders(localVariableNillableHeaders)

        let localVariableRequestBuilder: RequestBuilder<Void>.Type = openAPIClient.requestBuilderFactory.getNonDecodableBuilder()

        return localVariableRequestBuilder.init(method: "GET", URLString: (localVariableUrlComponents?.string ?? localVariableURLString), parameters: localVariableParameters, headers: localVariableHeaderParameters, requiresAuthentication: false, openAPIClient: openAPIClient)
    }

    /**
     Updated user
     
     - parameter username: (path) name that need to be deleted 
     - parameter body: (body) Updated user object 
     - parameter openAPIClient: The OpenAPIClient that contains the configuration for the http request.
     - returns: AnyPublisher<Void, Error>
     */
    #if canImport(Combine)
    @available(macOS 10.15, iOS 13.0, tvOS 13.0, watchOS 6.0, *)
    open class func updateUser(username: String, body: User, openAPIClient: OpenAPIClient = OpenAPIClient.shared) -> AnyPublisher<Void, Error> {
        let requestBuilder = updateUserWithRequestBuilder(username: username, body: body, openAPIClient: openAPIClient)
        let requestTask = requestBuilder.requestTask
        return Future<Void, Error> { promise in
            nonisolated(unsafe) let promise = promise
            requestBuilder.execute { result in
                switch result {
                case .success:
                    promise(.success(()))
                case let .failure(error):
                    promise(.failure(error))
                }
            }
        }
        .handleEvents(receiveCancel: {
            requestTask.cancel()
        })
        .eraseToAnyPublisher()
    }
    #endif

    /**
     Updated user
     - PUT /user/{username}
     - This can only be done by the logged in user.
     - parameter username: (path) name that need to be deleted 
     - parameter body: (body) Updated user object 
     - parameter openAPIClient: The OpenAPIClient that contains the configuration for the http request.
     - returns: RequestBuilder<Void> 
     */
    open class func updateUserWithRequestBuilder(username: String, body: User, openAPIClient: OpenAPIClient = OpenAPIClient.shared) -> RequestBuilder<Void> {
        var localVariablePath = "/user/{username}"
        let usernamePreEscape = "\(APIHelper.mapValueToPathItem(username))"
        let usernamePostEscape = usernamePreEscape.addingPercentEncoding(withAllowedCharacters: .urlPathAllowed) ?? ""
        localVariablePath = localVariablePath.replacingOccurrences(of: "{username}", with: usernamePostEscape, options: .literal, range: nil)
        let localVariableURLString = openAPIClient.basePath + localVariablePath
        let localVariableParameters = JSONEncodingHelper.encodingParameters(forEncodableObject: body, codableHelper: openAPIClient.codableHelper)

        let localVariableUrlComponents = URLComponents(string: localVariableURLString)

        let localVariableNillableHeaders: [String: Any?] = [
            :
        ]

        let localVariableHeaderParameters = APIHelper.rejectNilHeaders(localVariableNillableHeaders)

        let localVariableRequestBuilder: RequestBuilder<Void>.Type = openAPIClient.requestBuilderFactory.getNonDecodableBuilder()

        return localVariableRequestBuilder.init(method: "PUT", URLString: (localVariableUrlComponents?.string ?? localVariableURLString), parameters: localVariableParameters, headers: localVariableHeaderParameters, requiresAuthentication: false, openAPIClient: openAPIClient)
    }
}
