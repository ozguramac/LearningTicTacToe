// A function that attaches a button click handler
function enableClick() {
  document.getElementById('submitButton').onclick = function() {
//    var name = document.getElementById('nameInput').value;
//    gapi.client.myApi.sayHi({'name': name}).execute(
//      function(response) {
//        var outputAlertDiv = document.getElementById('outputAlert');
//        outputAlertDiv.style.visibility = 'visible';
//
//        if (!response.error) {
//          outputAlertDiv.className = 'alert alert-success';
//          outputAlertDiv.innerHTML = '<h2>' + response.result.data + '</h2>';
//        }
//        else if (response.error) {
//          outputAlertDiv.className = 'alert alert-danger';
//          outputAlertDiv.innerHTML = '<b>Error Code: </b>' + response.error.code + ' [' + response.error.message + ']';
//        }
//      }
//    );
    return false;
  }
}
// This is called initially
function init() {
  var apiName = 'myApi';
  var apiVersion = 'v1';
  var apiRoot = 'https://' + window.location.host + '/_ah/api';
  if (window.location.hostname == 'localhost'
      || window.location.hostname == '127.0.0.1'
      || ((window.location.port != "") && (window.location.port > 1023))) {
        // We're probably running against the DevAppServer
        apiRoot = 'http://' + window.location.host + '/_ah/api';
  }
  var callback = function() {
    enableClick();
  }
  gapi.client.load(apiName, apiVersion, callback, apiRoot);
}

