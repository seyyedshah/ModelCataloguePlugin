describe "mc.util.security", ->

  beforeEach module "mc.util.security"
  beforeEach module "ui.router"

  describe "default security doesn't provide any security constraints", ->

    it "returns default security service", inject (security, $rootScope) ->
      expect(security.mock).toBeTruthy()
      expect(security.isUserLoggedIn()).toBeTruthy()

      expect(security.login).toBeFunction()
      expect(security.logout).toBeFunction()

      expect(security.hasRole('CURATOR')).toBeTruthy()
      expect(security.hasRole('VIEWER')).toBeTruthy()

      user = security.getCurrentUser()

      expect(user).toBeDefined()
      expect(user.displayName).toBe('Anonymous Curator')


      newUser = null

      security.login().then (_newUser_) ->
        newUser = _newUser_

      expect(newUser).toBeNull()

      $rootScope.$digest()

      expect(angular.equals(newUser, user)).toBeTruthy()

  describe "read only security provides user with the role VIEWER", ->
    beforeEach module (securityProvider) ->
      securityProvider.readOnly()
      return

    it "returns read only security service", inject (security, $rootScope) ->

      expect(security.mock).toBeTruthy()
      expect(security.isUserLoggedIn()).toBeTruthy()

      expect(security.login).toBeFunction()
      expect(security.logout).toBeFunction()


      expect(security.hasRole('CURATOR')).toBeFalsy()
      expect(security.hasRole('VIEWER')).toBeTruthy()

      user = security.getCurrentUser()

      expect(user).toBeDefined()
      expect(user.displayName).toBe('Anonymous Viewer')

      newUser = null

      security.login().then (_newUser_) ->
        newUser = _newUser_

      expect(newUser).toBeNull()

      $rootScope.$digest()

      expect(angular.equals(newUser, user)).toBeTruthy()


  describe "can setup own provider", ->
    beforeEach module (securityProvider) ->
      securityProvider.setup ['$log', ($log) ->
        security =
          isUserLoggedIn: -> true
          getCurrentUser: -> { displayName: 'Horrible Monster', hasRole: (role) -> role == 'BOO' }
          hasRole: (role) -> role == 'BOO'
          login: (username, password, rememberMe = false) -> security.getCurrentUser()
          logout: -> $log.info "Logout requested on custom security service"
      ]
      return

    it "returns read only security service", inject (security, $rootScope) ->

      expect(security.mock).toBeUndefined()
      expect(security.isUserLoggedIn()).toBeTruthy()

      expect(security.login).toBeFunction()
      expect(security.logout).toBeFunction()

      user = security.getCurrentUser()

      expect(user).toBeDefined()
      expect(user.displayName).toBe('Horrible Monster')

      newUser = null

      security.login().then (_newUser_) ->
        newUser = _newUser_

      expect(newUser).toBeNull()

      $rootScope.$digest()

      expect(angular.equals(newUser, user)).toBeTruthy()

  describe "can setup spring security", ->

    $httpBackend = null

    beforeEach module (securityProvider) ->
      config =
        contextPath: 'myApp'
        roles:
          VIEWER:     ['ROLE_USER', 'ROLE_METADATA_CURATOR', 'ROLE_ADMIN'],
          CURATOR:    ['ROLE_METADATA_CURATOR', 'ROLE_ADMIN'],
          ADMIN:      ['ROLE_ADMIN']
        currentUser:
          roles:  "ROLE_USER",
          username: "currentOnlineUser"
      securityProvider.springSecurity config
      return

    beforeEach inject ($injector) ->
      $httpBackend = $injector.get '$httpBackend'
      result =
        status: 200
        success : true
        username: 'loggingUser1'
        roles: ['ROLE_USER']
      params =
        username:'loggingUser1'
        password:'123'

      # Mock the http call for login
      request = 'myApp/j_spring_security_check?ajax=true&j_password=123&j_username=loggingUser1'
      $httpBackend.when('POST',request,params).respond(result)


    afterEach ()->
      $httpBackend.verifyNoOutstandingExpectation();
      $httpBackend.verifyNoOutstandingRequest();


    it "returns a service for spring security", inject (security, $rootScope) ->

      expect(security.mock).toBeUndefined()
      expect(security.isUserLoggedIn()).toBeTruthy()

      expect(security.login).toBeFunction()
      expect(security.logout).toBeFunction()

      expect(security.getCurrentUser).toBeFunction()
      expect(security.hasRole).toBeFunction()

      expect(security.changePassword).toBeFunction()


    it "login returns current user login detail", inject (security, $rootScope,$http) ->

      userName = "loggingUser1"
      password = "123"

      security.login(userName, password)
      $httpBackend.flush();

      user = security.getCurrentUser()
      expect(user).toBeDefined()
      expect(user.username).toBe('loggingUser1')
      expect(user.roles[0]).toBe('ROLE_USER')
      expect(user.roles[1]).toBe('VIEWER')
