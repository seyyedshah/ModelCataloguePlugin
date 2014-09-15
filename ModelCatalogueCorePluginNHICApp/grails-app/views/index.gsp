<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
</head>

<body>
<div class="container">
    <div class="row">
        <messages-panel max="3" growl="true"></messages-panel>
    </div>

    <div class="row">
        <div class="col-md-12">
            <ui-view></ui-view>
        </div>
    </div>

    <div id="push"></div>
</div>
</body>
</html>




