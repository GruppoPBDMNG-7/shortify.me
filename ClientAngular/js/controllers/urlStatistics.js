app.controller('urlStatistics', function($scope, $rootScope, $http) {
    
    $rootScope.notStatisticsView = true;
    $rootScope.textUrlStatistics = "Inspect a Shortify URL";
    
    //Dati di esempio utilizzati per la costruzione del grafico
    //x
    $rootScope.labels = [];
    //Casistiche
    $rootScope.series = [];
    //y
    $rootScope.data = [];
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
                $http.post("http://localhost:4567/api/v1/stats", {shorturl: urlforstat})
                .success(function(response) {
                    
                    var hourCounters = response.hourCounters;
                    var uniqueCounter = response.uniqueCounter;
                    var countryCounters = response.countryCounters;
                    var dayCounters = response.dayCounters;
                    
                    var labels = Object.keys(hourCounters).sort();
                    var perHour = [];
                    var total = [];
                    var day = labels[0].split(" ")[0];
                    for (var i = 0; i < labels.length; i++) {
                        perHour.push(hourCounters[labels[i]]);
                        total.push(dayCounters[day]);
                    }
                    
                    $rootScope.labels = labels;
                    $rootScope.series = ['Hour', 'Total'];
                    $rootScope.data = [perHour, total];
                    
                })    
                .error(function(response) {
                    console.log("Error");
                    
                });
                
                
                /*TEST WITH LOCAL JSON
                $http.get("js/controllers/response.json")
                .then(function(response){
                    var hourCounters = response.data.hourCounters;
                    var uniqueCounter = response.data.uniqueCounter;
                    var countryCounters = response.data.countryCounters;
                    var dayCounters = response.data.dayCounters;
                    
                    
                    var labels = Object.keys(hourCounters).sort();
                    var perHour = [];
                    var total = [];
                    
                    var day = labels[0].split(" ")[0];
                    for (var i = 0; i < labels.length; i++) {
                        perHour.push(hourCounters[labels[i]]);
                        total.push(dayCounters[day]);
                    }
                    
                    $rootScope.labels = labels;
                    $rootScope.series = ['Hour', 'Total'];
                    $rootScope.data = [perHour, total];
                    
                });*/
                
                
                $rootScope.notStatisticsView = false;
                document.getElementById("statisticsDiv").setAttribute("class", "centeredText animated fadeIn");
            } else {
                $rootScope.notStatisticsView = true;
            
            }
            
        }
    }
    
});