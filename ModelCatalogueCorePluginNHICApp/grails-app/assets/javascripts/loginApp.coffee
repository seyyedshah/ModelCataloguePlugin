
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
#= require modalPromptLogin

loginApp = angular.module('loginApp', [
  'demo.config'
  'mc.core.ui.bs'
  'ui.bootstrap'
  'angular-loading-bar'
  'ngAnimate'
])
#'mc.core.ui.states'


loginApp.controller('logoutCtrl', ['$scope', 'security', ($scope, security)->
  $scope.logout = ->
    security.logout()
])


loginApp.controller('loginCtrl', ['security', '$scope', (security, $scope)->
  $scope.login = ->
    security.requireLogin()
])