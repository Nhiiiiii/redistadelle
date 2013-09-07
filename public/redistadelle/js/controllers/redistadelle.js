function RedistadelleCtrl($scope, $http) {
	$http.defaults.useXDomain = true;
	
	$scope.getPlayersURL="/players/1";
	$scope.killURL="/kill/1/";
	$scope.stoleURL="/stole/1/";
	
	$scope.otherPlayers = [];
	
	$scope.getPlayers = function() {
		$http({method: 'GET', url: $scope.getPlayersURL}).
			success(function(data, status, headers, config) {			
				$scope.player = data[0];
				for (var i = 1; i < data.length; i++) {
				    $scope.otherPlayers.push(data[i]);
				}
			}).
			error(function(data, status, headers, config) {
				// TODO msg d'erreur
			});
	};

	$scope.player = $scope.getPlayers();
	
	$scope.kill = function() {
		$http({method: 'POST', url: $scope.killURL + $scope.selected}).
		success(function(data, status, headers, config) {
			$scope.getPlayers();
		}).
		error(function(data, status, headers, config) {
			alert("KO");
			// TODO msg d'erreur
		});
		
	}
	
	$scope.stole = function() {
		$http({method: 'POST', url: $scope.stoleURL + $scope.selected}).
		success(function(data, status, headers, config) {
			$scope.getPlayers();
		}).
		error(function(data, status, headers, config) {
			alert("KO");
			// TODO msg d'erreur
		});
	}
	
}