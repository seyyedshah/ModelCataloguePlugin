<html>
<head>
    <asset:stylesheet href="metaDataCurator.css"/>
    <asset:stylesheet href="application.css"/>
    <asset:javascript src="angular/angular.js"/>
</head>

<body>
<div id="wrap">
    <div class="navbar navbar-default navbar-fixed-top" role="navigation">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="${createLink(uri: '/')}">Model Catalogue</a>
            </div>
        </div>
    </div>


    <div class="container" ng-app="resetPasswordApp" ng-controller="resetPasswordCtrl">
        <div id="info" class="row">
            <div class="col-sm-4 col-sm-offset-4">
                <div class="panel panel-default">
                    <!-- Default panel contents -->
                    <div class="panel-heading">
                    </div>

                    <div class="panel-body">

                        <g:if test='${flash.error}'>
                            <div class="alert alert-danger" role="alert">
                                ${flash.error}
                            </div>
                        </g:if>

                        <form action='resetPassword' method='POST' id='loginForm' class='cssform' autocomplete='off'
                              name='resetPasswordForm'>
                            <g:hiddenField name='t' value='${token}'/>

                            <div class="sign-in">

                                <div class="form-group">
                                    <label for="password">Password</label>
                                    <input type="password" class="form-control" id="password" name='password'
                                           placeholder="Password" value="${command?.password}" ng-model="password">
                                </div>

                                <div class="form-group">
                                    <label for="password2">Password(again)</label>
                                    <input type="password" class="form-control" id="password2" name='password2'
                                           placeholder="Password(again)" value="${command?.password2}"
                                           ng-model="password2">
                                </div>

                                <button type='submit' class="btn btn-success" id="submit"
                                        ng-disabled="!password || !password2"><span
                                        class="glyphicon glyphicon-ok"></span> Update my password</button>
                            </div>
                        </form>

                    </div>
                </div>
            </div>
        </div>
    </div>

</div>

<div id="footer">
    <div class="container">
        <p class="muted credit">

        <div class="row">
            <div class="col-sm-4 nav-left">
                <div show-if-logged-in>
                    <p class="feedback text-muted" id="feedback"></p>
                </div>
            </div>

            <div class="col-sm-4"><p
                    class="text-muted">Model Catalogue &copy; ${new Date()[Calendar.YEAR]} &nbsp&nbspv<g:meta
                        name="app.version"/></p></div>

            <div class="col-sm-4"></div>
        </div>
    </p>
    </div>
</div>

<script>
    $(document).ready(function () {
        $('#password').focus();
    });

</script>


<script type="text/javascript">
    var demoConfig = angular.module('resetPasswordApp', [])
    demoConfig.controller('resetPasswordCtrl', ["$scope", function ($scope) {
    }])
</script>

</body>
</html>