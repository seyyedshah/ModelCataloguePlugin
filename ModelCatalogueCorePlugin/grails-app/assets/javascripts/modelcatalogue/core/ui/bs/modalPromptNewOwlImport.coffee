angular.module('mc.core.ui.bs.modalPromptNewOwlImport', ['mc.util.messages', 'angularFileUpload']).config ['messagesProvider', (messagesProvider)->
  factory = [ '$modal', ($modal) ->
    (title, body, args) ->
      dialog = $modal.open {
        windowClass: 'basic-edit-modal-prompt'
        backdrop: 'static'
        keyboard: false
        resolve:
          args: -> args
        template: '''
         <div class="modal-header">
            <h4>''' + title + '''</h4>
        </div>
        <div class="modal-body">
            <messages-panel messages="messages"></messages-panel>
            <form role="form" ng-submit="saveElement()">
              <div class="form-group">
                <label for="name" class="">OWL Model Name</label>
                <input type="text" class="form-control" id="name" placeholder="OWL Model Name (leave blank to use filename)" ng-model="copy.name">
              </div>
              <div class="form-group">
                <label for="asset" class="">OWL File (.owl or .ttl)</label>
                <input ng-hide="uploading &amp;&amp; progress" type="file" accept=".ttl,.owl" class="form-control" id="asset" placeholder="File" ng-model="copy.asset" ng-file-select="onFileSelect($files)">
                <progressbar value="progress" ng-show="uploading &amp;&amp; progress">{{progress}} %</progressbar>
              </div>
            </form>
        </div>
        <div class="modal-footer">
            <button class="btn btn-success" ng-click="saveElement()" ng-disabled="!hasChanged() || uploading"><span class="glyphicon glyphicon-ok"></span> Save</button>
            <button class="btn btn-warning" ng-click="cancel()">Cancel</button>
        </div>
        '''
        controller: 'importCtrl'

      }

      dialog.result
  ]
  messagesProvider.setPromptFactory 'new-owl-import', factory
]