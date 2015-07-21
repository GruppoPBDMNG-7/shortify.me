app.controller('shortingCtrl', function($scope, $rootScope, $http, $timeout) {
    
    $rootScope.done = false;
    $rootScope.error = false;
    
    $scope.customURLView = false;
    $scope.showSpinner = false;
    $scope.textCustomURL = "Make your custom URL";
    
    /*
     * Converte un url in uno short e memorizza i risultati nella variabile shorturl.
     * Viene effettuata una richiesta in POST al server che si occupa della conversione.
     */
    $scope.convert = function(url, text) {
        
        $scope.showSpinner = true;
        
        if (url != "" && url !=  null) {
            $rootScope.error = false;
            $rootScope.notStatisticsView = true;

            //controllo per far scomparire il risultato di una conversione precedente
            if($scope.done) {
                document
                    .getElementById("resultDiv")
                    .setAttribute("class", "alert alert-success centeredText animated fadeOut");  
            }

            //timeout per far concludere l'animazione
            $timeout(function() {
                
                //richiesta in post, il secondo argomento è il json che viene trasferito
                $http.post("http://localhost:4567/api/v1/convert", {longurl: url, customText: text})
                .success(function(response) { 

                    $scope.shorturl = response.shortUrl;

                    //spostamento verso l'alto del container principale
                    //document.getElementById("container").setAttribute("class", "animated fadeOutUpBig");

                    //risultato con animazione
                    document.getElementById("resultDiv").setAttribute("class", "alert alert-success centeredText animated fadeIn");  
                    
                    $scope.showSpinner = false;
                    $rootScope.done = true;
                })
                .error(function(response) {
                    $rootScope.textError = response.error;
                    document.getElementById("errorDiv").setAttribute("class", "alert alert-danger centeredText animated fadeIn");  
                    $scope.showSpinner = false;
                    $rootScope.error = true;
                });
            }, 1000);
            
        } else {
            $scope.showSpinner = false;
        }
    }
    
    $scope.showCustomURLView = function() {
        if(!$scope.customURLView) {
            $scope.customURLView = true;
            $scope.textCustomURL = "Shortify an URL";
            document.getElementById("customURLDiv").setAttribute("class", "centeredText animated fadeIn");  
        } else {
            $scope.customURLView = false;
            $scope.textCustomURL = "Make your custom URL";
            document.getElementById("longURLDiv").setAttribute("class", "centeredText animated fadeIn");  
        }
    }
});

