#= require_self
#= require hideForRole
#= require showForRole
#= require hideIfLoggedIn
#= require showIfLoggedIn
#= require contextualActions
#= require actions
#= require actionButton
#= require contextualActions

angular.module 'mc.util.ui', [
  # dependencies
  'mc.util'

  # list of modules
  'mc.util.ui.hideForRole'
  'mc.util.ui.showForRole'
  'mc.util.ui.hideIfLoggedIn'
  'mc.util.ui.showIfLoggedIn'
  'mc.util.ui.contextualActions'
  'mc.util.ui.actions'
  'mc.util.ui.actionButton'
  'mc.util.ui.contextualActions'
]