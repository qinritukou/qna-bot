/**
 * 
 */

var app = angular.module("app", ["ngRoute"]);

app.config(['$routeProvider', function($routeProvider) {
    $routeProvider
    .when("/Main", {
        templateUrl: "Main.html"
    })
    .otherwise({
        redirectTo: '/Main'
    });
    
    
}]);