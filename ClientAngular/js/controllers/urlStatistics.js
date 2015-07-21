app.controller('urlStatistics', function($scope, $rootScope, $http) {
    
    $rootScope.notStatisticsView = true;
    $rootScope.textUrlStatistics = "Inspect a Shortify URL";
    
    //x
    $rootScope.labels = [];
    //Casistiche
    $rootScope.series = [];
    //y
    $rootScope.data = [];
    
    /**Funzione per la richiesta delle informazioni riguardanti uno
    specifico shorturl e la visualizzazione del grafuico associato*/
    $rootScope.showStatistics = function(urlforstat) {
        if (urlforstat == "" || urlforstat == null) {
            return; //Campo non avvalorato
        } else {
            if($rootScope.notStatisticsView) {
                
                //Richiesta delle informazioni associate ad uno specifico shorturl
                $http.post("http://localhost:4567/api/v1/stats", {shorturl: urlforstat})
                .success(function(response) {
                      
                    $rootScope.error = false;
                    
                    //Ottengo dati per statistiche dalla response
                    var hourCounters = response.hourCounters;
                    $scope.uniqueCounter = response.uniqueCounter;
                    var countryCounters = response.countryCounters;
                    dayCounters = response.dayCounters;
                    $scope.longUrl = response.longUrl;
                    $scope.shortUrl = response.shortUrl;
                    
                    //BAR CHART statistiche intervalli orari
                    var labels = Object.keys(hourCounters).sort();
                    var perHour = [];   //Visite per ora
                    
                    //Riepimento delle liste di valori per la rappresentazione grafica
                    for (var i = 0; i < labels.length; i++) {
                        perHour.push(hourCounters[labels[i]]);
                    }
                    
                    //Dati utili per il grafico canvas
                    $rootScope.labels = labels;
                    $rootScope.series = ['Visits per hour'];
                    $rootScope.data = [perHour];
                    
                   
                    //TABELLA VISITE GIORNALIERE
                    var days = Object.keys(dayCounters).reverse();
                    for (var i = 0; i < days.length; i++) {
                        var count = dayCounters[days[i]];
                        var table = document.getElementById("days-table-rows");
                        var row = table.insertRow(i);
                        row.setAttribute("class", "success");
                        var d = row.insertCell(0);
                        var v = row.insertCell(1);

                        d.innerHTML = days[i];
                        v.innerHTML = count;
                    }
                    
                    //TABELLA VISITE PER NAZIONE
                    var countries = Object.keys(countryCounters);
                    for (var i = 0; i < countries.length; i++) {
                        var count = countryCounters[countries[i]];
                        var table = document.getElementById("country-table-rows");
                        var row = table.insertRow(i);
                        row.setAttribute("class", "success");
                        var c = row.insertCell(0);
                        var v = row.insertCell(1);
                        
                        if (countries[i] != "NULL") {
                            c.innerHTML = '<span class="flag-icon flag-icon-' + 
                                countries[i].toLowerCase() + '"></span>';
                            v.innerHTML = count;
                            
                        } else {
                            c.innerHTML = "UNDEFINED";
                            v.innerHTML = count;
                            
                        }
                    }
                               
                    //visualizza le statistiche
                    $rootScope.notStatisticsView = false;
                    
                    //sposta la vista verso il div
                    $('html, body').animate({scrollTop:$('#statisticsDiv').position().top}, 'slow');
       
                })    
                .error(function(response) {
                    $rootScope.done = false;
                    $rootScope.textError = response.error;
                    document.getElementById("errorDiv").setAttribute("class", "alert alert-danger centeredText animated fadeIn");  
                    $rootScope.error = true;               
                });
   
                    
            }
            
        }
    }
    
    $scope.hideStatistics = function() {
        
        //PULIZIA ELIMINANDO RIGHE AGGIUNTE ALLE TABELLE
        var countryTableRows = document.getElementById("country-table-rows");
        while (countryTableRows.firstChild) {
            countryTableRows.removeChild(countryTableRows.firstChild);
        }

        var daysTableRows = document.getElementById("days-table-rows");
        while (daysTableRows.firstChild) {
            daysTableRows.removeChild(daysTableRows.firstChild);
        }
        
        $rootScope.notStatisticsView = true;
    }
    
});
