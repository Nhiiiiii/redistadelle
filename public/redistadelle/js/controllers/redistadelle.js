function RedistadelleCtrl($scope, $http) {
	$scope.url="/player/1/1";

	$http.defaults.useXDomain = true;
	$scope.otherPlayers = [
		{"num":2, "name":"tete", "gold":2,"job":"assassin","hand":["port","taverne"],"city":["port","taverne"]},
		{"num":3, "name":"titi", "gold":0,"job":"magicien","hand":["port","taverne"],"city":["port","taverne"]},
		{"num":4, "name":"toto", "gold":5,"job":"marchand","hand":["port","taverne"],"city":["port","taverne"]},
		{"num":5, "name":"tutu", "gold":6, "job": "roi", "hand": ["port","taverne"], "city": ["port","taverne"]},
		{"num":6, "name":"tyty", "gold":1,"job":"eveque","hand":["port","taverne"],"city":["port","taverne"]},
		{"num":7, "name":"tztz", "gold":0,"job":"architecte","hand":["port","taverne"],"city":["port","taverne"]},
		{"num":8, "name":"Obiwan", "gold":3,"job":"condotiere","hand":["port","taverne"],"city":["port","taverne"]}];
	
	$scope.player = {"num": 1, "name":"tata", "gold":2, "job": "voleur", "hand": ["port","taverne"], "city": ["caserne","draco-port"]};
	
	$scope.toto = function() {
		$http({method: 'GET', url: $scope.url}).
			success(function(data, status, headers, config) {
				$scope.status = status;
				$scope.data = data;
				$scope.headers = headers;
				$scope.config = config;
				
				$scope.player = data;
			}).
			error(function(data, status, headers, config) {
				$scope.status = status;
				$scope.data = data || "Erreur";
				$scope.headers = headers;
				$scope.config = config;
			});
	};

}