app.controller('urlStatistics', function($scope, $rootScope, $http) {
    
    $rootScope.notStatisticsView = true;
    $rootScope.textUrlStatistics = "Inspect a Shortify URL";
    
    //Dati di esempio utilizzati per la costruzione del grafico
    //x
    $rootScope.labels = ['2006', '2007', '2008', '2009', '2010', '2011', '2012'];
    //Casistiche
    $rootScope.series = ['Series A', 'Series B'];
    //y
    $rootScope.data = [
        [65, 59, 80, 81, 56, 55, 40],
        [28, 48, 40, 19, 86, 27, 90]
    ];
    //Opzioni eventuali del grafico da specificare
    $rootScope.options = {};
    
    /**Funzione per la richiesta delle informazioni riguardanti uno
    specifico shorturl e la visualizzazione del grafuico associato*/
    $rootScope.showStatistics = function(urlforstat) {
        if (urlforstat == null) {
            return; //Campo non avvalorato
        } else {
            if($rootScope.notStatisticsView) {
                
                //Richiesta delle informazioni associate ad uno specifico shorturl
                /**$http.post("http://localhost:4567/inspect", {shorturl: urlforstat})
                .success(function(response) {
                    $rootScope.data = response.data;
                })
                .error(function(response) {
                });*/
                
                $rootScope.notStatisticsView = false;
                document.getElementById("statisticsDiv").setAttribute("class", "centeredText animated fadeIn");
            } else {
                $rootScope.notStatisticsView = true;
            
            }
            
        }
    }
    
});