angular.module('mc.core.ui.bs.modalPromptForgotPassword', ['mc.util.messages', 'ngCookies']).config ['messagesProvider', (messagesProvider)->
  factory = [ '$modal', '$q', 'messages', 'security', ($modal, $q, messages, security) ->
    ->
      dialog = $modal.open {
        windowClass: 'login-modal-prompt'
        template: '''
         <div class="modal-header">
            <h4>Login</h4>
        </div>
        <div class="modal-body">
            <messages-panel messages="messages"></messages-panel>
            <form role="form" ng-submit="login()">
              <div class="form-group">
                <label for="username">Enter your username and we'll send a link to reset your password to the address we have for your account.</label>
                <input type="text" class="form-control" id="username" placeholder="Username" ng-model="user.username">
              </div>
              <button type="submit" class="hide" ng-click="forgotPassword()"></button>
            </form>
        </div>
        <div class="modal-footer">
            <button type="submit" class="btn btn-success" ng-click="forgotPassword()" ng-disabled="!user.username"><span class="glyphicon glyphicon-ok"></span>Reset my password</button>
            <button class="btn btn-warning" ng-click="$dismiss()">Cancel</button>
        </div>
        '''
        controller: ['$scope', '$cookies', 'messages', 'security', '$modalInstance', '$log',
          ($scope, $cookies, messages, security, $modalInstance) ->
            $scope.user = {}
            $scope.messages = messages.createNewMessages()

            $scope.forgotPassword = ->
              security.forgotPassword($scope.user.username).then (success)->
                if success.data.error
                  $scope.messages.error success.data.error
                else if success.data.errors
                  for error in success.data.errors
                    $scope.messages.error error
                else
                  $cookies.mc_remember_me = $scope.user.rememberMe
                  $modalInstance.close success
                  messages.success('Your password reset email was sent - check your mail!','')
        ]

      }

      dialog.result
  ]

  messagesProvider.setPromptFactory 'forgot-password', factory
]