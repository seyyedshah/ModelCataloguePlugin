<html>
<head>
    <meta name='layout' content='main'/>
    <title><g:message code="springSecurity.login.title"/></title>
    <asset:stylesheet href="metaDataCurator.css"/>
    <asset:stylesheet href="application.css"/>

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
                <a class="navbar-brand" href="/NHICApp">Model Catalogue</a>
            </div>

        </div>
    </div>

    <div class="container">
        <div id="info" class="row">
            <div class="col-sm-4 col-sm-offset-4">
                <div class="panel panel-default">
                    <!-- Default panel contents -->
                    <div class="panel-heading">Login</div>

                    <div class="panel-body">

                        <g:if test='${flash.message}'>
                            <div class="alert alert-danger" messages="messages">
                                <div class='login_message'>${flash.message}</div>
                            </div>
                        </g:if>


                        <form action='${postUrl}' method='POST' id='loginForm' class='cssform' autocomplete='off'>
                            <div class="form-group">
                                <label for="username">Username</label>
                                <input type="text" class="form-control" id="username" name='j_username'
                                       placeholder="Username">
                            </div>

                            <div class="form-group">
                                <label for="password">Password</label>
                                <input type="password" class="form-control" id="password" name='j_password'
                                       placeholder="Password">
                            </div>

                            <p id="remember_me_holder">
                                <input type='checkbox' class='chk' name='${rememberMeParameter}' id='remember_me'
                                       <g:if test='${hasCookie}'>checked='checked'</g:if>/> Remember Me
                            </p>



                            <button type='submit' class="btn btn-success" id="submit"><span
                                    class="glyphicon glyphicon-ok"></span> Login</button>

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
                    <p class="feedback text-muted" id="feedback">Feedback</p>
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


<asset:javascript src="jiraIssueTracker.js"></asset:javascript>

<script type='text/javascript'>
    <!--
    (function () {
        document.forms['loginForm'].elements['j_username'].focus();
    })();
    // -->
</script>

</body>
</html>
