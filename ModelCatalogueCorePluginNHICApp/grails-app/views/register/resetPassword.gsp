<html>
<head>
    <title><g:message code='spring.security.ui.resetPassword.title'/></title>
    <asset:stylesheet href="metaDataCurator.css"/>
    <meta name='layout' content='main'/>

</head>
<body>

<div class="container" >
    <div id="info" class="row">
        <div class="col-sm-4 col-sm-offset-4">
            <div class="panel panel-default">
                <!-- Default panel contents -->
                <div class="panel-heading">
                    <g:message code='spring.security.ui.resetPassword.title'/>
                </div>

                <div class="panel-body">

                    <g:if test='${flash.error}'>
                        <div class="alert alert-danger" role="alert">
                            ${flash.error}
                        </div>
                    </g:if>


                    <form action='resetPassword' method='POST' id='loginForm' class='cssform' autocomplete='off' name='resetPasswordForm'>
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
                                       placeholder="Password(again)" value="${command?.password2}" ng-model="password2">
                            </div>

                            <button type='submit' class="btn btn-success" id="submit" ng-disabled="!password || !password2"><span
                                    class="glyphicon glyphicon-ok"></span> Update my password</button>
                        </div>
                    </form>




                </div>
            </div>
        </div>
    </div>
</div>



%{--<s2ui:form width='475' height='250' elementId='resetPasswordFormContainer'--}%
           %{--titleCode='spring.security.ui.resetPassword.header' center='true'>--}%
%{----}%
    %{--<g:form action='resetPassword' name='resetPasswordForm' autocomplete='off'>--}%
        %{--<g:hiddenField name='t' value='${token}'/>--}%
        %{--<div class="sign-in">--}%
%{----}%
            %{--<br/>--}%
%{----}%
            %{--<table>--}%
                %{--<s2ui:passwordFieldRow name='password' labelCode='resetPasswordCommand.password.label' bean="${command}"--}%
                                       %{--labelCodeDefault='Password' value="${command?.password}"/>--}%
%{----}%
                %{--<s2ui:passwordFieldRow name='password2' labelCode='resetPasswordCommand.password2.label'--}%
                                       %{--bean="${command}"--}%
                                       %{--labelCodeDefault='Password (again)' value="${command?.password2}"/>--}%
            %{--</table>--}%
%{----}%
            %{--<s2ui:submitButton elementId='reset' form='resetPasswordForm'--}%
                               %{--messageCode='spring.security.ui.resetPassword.submit'/>--}%
%{----}%
        %{--</div>--}%
    %{--</g:form>--}%
%{----}%
%{--</s2ui:form>--}%

<script>
    $(document).ready(function () {
        $('#password').focus();
    });
</script>

</body>
</html>
