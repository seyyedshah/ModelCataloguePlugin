<%@ page contentType="text/html;charset=UTF-8" defaultCodec="none" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Model Catalogue NHIC App</title>
    <asset:stylesheet href="metaDataCurator.css"/>
    <asset:javascript src="loginApp.js"/>
    <script type="text/javascript">
        var demoConfig = angular.module('demo.config', ['mc.core.modelCatalogueApiRoot', 'mc.util.security'])
        demoConfig.config(['securityProvider', function (securityProvider) {
            securityProvider.springSecurity({
                contextPath: '${request.contextPath ?: ''}',
                roles: {
                    VIEWER: ['ROLE_USER', 'ROLE_METADATA_CURATOR', 'ROLE_ADMIN'],
                    CURATOR: ['ROLE_METADATA_CURATOR', 'ROLE_ADMIN'],
                    ADMIN: ['ROLE_ADMIN']
                },
                <sec:ifLoggedIn>
                currentUser: {
                    roles: ${grails.plugin.springsecurity.SpringSecurityUtils.getPrincipalAuthorities()*.authority.encodeAsJSON()},
                    username: '${sec.username()}'
                }
                </sec:ifLoggedIn>
            })
        }])
        demoConfig.value('modelCatalogueApiRoot', '${request.contextPath ?: ''}/api/modelCatalogue/core')
    </script>

</head>

<body>

<div id="loginApp" ng-app="loginApp">
    <div class="navbar navbar-default navbar-fixed-top" role="navigation">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#">Model Catalogue</a>
            </div>

            <div class="navbar-collapse collapse">

                <ul class="nav navbar-nav">
                    <li><a href="app/">Dashboard</a></li>
                </ul>
                <form show-if-logged-in class="navbar-form navbar-right" ng-submit="logout()"
                      ng-controller="logoutCtrl">
                    <button class="btn btn-danger" type="submit"><i class="glyphicon glyphicon-log-out"></i></button>
                </form>
            </div><!--/.nav-collapse -->
        </div>
    </div>

    <div class="container">
        <div class="row">

            <messages-panel max="3" growl="true"></messages-panel>


            <div class="jumbotron">
                <h1>Model Catalogue</h1>

                <p class="lead">
                    <b><em>Model</em></b> existing business processes and context. <b><em>Design</em></b>
                    new pathways, forms, data storage, studies. <b><em>Generate</em></b> better
                software components
                </p>

                %{--<form hide-if-logged-in ng-submit="login()" ng-controller="loginCtrl">--}%
                %{--<button class="btn btn-large btn-primary" type="submit">Login <i class="glyphicon glyphicon-log-in"></i></button>--}%
                %{--</form>--}%

                <form hide-if-logged-in>
                    <g:link data-placement="bottom" class="btn btn-primary" data-original-title="Logout" rel="tooltip"
                            controller="login" action="auth">Login
                        <i class="glyphicon glyphicon-log-in"></i>
                    </g:link>
                </form>

            </div>

            <!-- Example row of columns -->
            <div id="info" class="row">
                <div class="col-sm-4">
                    <h2>Architecture</h2>

                    <p>Track your data elements from collection - model services,
                    databases and warehouses. Generate your own feeds, and generate
                    components for integration engines.</p>

                    <p>
                        <a href="#">More info&hellip;</a>
                    </p>
                </div>

                <div class="col-sm-4">
                    <h2>Forms</h2>

                    <p>Build forms from standard data elements in our friendly
                    drag-n-drop interface. Export your forms to your favourite tool.</p>

                    <p>
                        <a href="#">Coming soon&hellip;</a>
                    </p>
                </div>

                <div class="col-sm-4">
                    <h2>Pathways</h2>

                    <p>Design your workflows and visualise your patient pathways.
                    Annotate nodes with data elements, forms, and decisions.
                    Automatically build databases, dashboard interfaces and reporting
                    data.</p>

                    <p>
                        <a href="#">Coming soon&hellip;</a>
                    </p>
                </div>

            </div>
        </div>
    </div>

</div>

</body>
</html>