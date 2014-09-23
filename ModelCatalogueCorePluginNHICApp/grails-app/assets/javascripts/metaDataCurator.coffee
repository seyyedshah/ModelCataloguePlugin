
#= require jquery/dist/jquery
#= require bootstrap/dist/js/bootstrap
#= require ng-file-upload-shim/angular-file-upload-shim
#= require angular/angular
#= require angular-i18n/angular-locale_en-gb
#= require angular-animate/angular-animate
#= require ng-file-upload/angular-file-upload
#= require angular-http-auth/src/http-auth-interceptor
#= require angular-loading-bar/build/loading-bar
#= require angular-ui-router/release/angular-ui-router
#= require modelcatalogue/util/index
#= require modelcatalogue/util/ui/index
#= require modelcatalogue/core/index
#= require modelcatalogue/core/ui/index
#= require modelcatalogue/core/ui/states/index
#= require modelcatalogue/core/ui/bs/index
#= require modalPromptChangePassword
#= require modalPromptRegister
#= require modalPromptLogin
#= require modalPromptForgotPassword

@grailsAppName = 'model_catalogue'

metadataCurator = angular.module('metadataCurator', [
  'demo.config'
  'mc.core.ui.bs'
  'mc.core.ui.states'
  'ui.bootstrap'
  'angular-loading-bar'
  'ngAnimate',
  'mc.core.ui.bs.modalPromptChangePassword',
  'mc.core.ui.bs.modalPromptRegister',
  'mc.core.ui.bs.modalPromptForgotPassword'
])

metadataCurator.config ['$stateProvider', '$urlRouterProvider', ($stateProvider, $urlRouterProvider)->
  $urlRouterProvider.otherwise("/dashboard")
]




metadataCurator.controller('defaultStates.registerCtrl', ['$scope', 'security', 'messages', ($scope, security,messages)->

  $scope.register = ->
    messages.prompt('register', null, type: 'register')

  $scope.forgotPassword = ->
    messages.prompt('forgot-password', null, type: 'forgot-password')

])


metadataCurator.controller('defaultStates.changePasswordCtrl', ['messages', '$scope', (messages, $scope)->
  $scope.changePassword = ->
    messages.prompt('change-password', null, type: 'change-password')
#    .then (success)->
#      debugger
#    , ->
#      debugger
])