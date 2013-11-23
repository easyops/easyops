
var app = angular.module('eomApp', ['ngRoute']);
var routers = [
    '/home',
    '/createUser',
    '/associateHost',
    '/deployment',
    '/initEnv'
];

app.config(['$routeProvider', function($routeProvider) {
    $routeProvider.
        when('/home', {
            controller: 'homeController',
            templateUrl: '/templates/home.html'
        })
        .when('/createUser', {
            controller: 'createUserController',
            templateUrl: '/templates/createUser.html'
        })
        .when('/associateHost', {
            controller: 'associateHostController',
            templateUrl: '/templates/associateHost.html'
        })
        .when('/deployment', {
            controller: 'deploymentController',
            templateUrl: '/templates/deployment.html'
        })
        .when('/initEnv', {
            controller: 'initEnvController',
            templateUrl: '/templates/initEnv.html'
        })
        .otherwise({redirectTo:'/home'});
}]).run(['$rootScope', '$location', function($rootScope, $location) {
    $rootScope.breadcrumb = {};

    $rootScope.$on('$locationChangeStart', function(scope, next, current){
        var currentRouter = $location.path();
        if (currentRouter && !$rootScope.checkRouterAvailable(currentRouter)) {
            scope.preventDefault();
        }
    });

    $rootScope.$on('$locationChangeSuccess', function(event, newUrl, oldUrl) {
    });

    $rootScope.updateRouter = function(path, available) {
        $rootScope[path] = typeof available === 'undefined'? false: true;
    };

    $rootScope.checkRouterAvailable = function(path) {
        return $rootScope[path];
    };

    $rootScope.updateRouter('/home', true);

    $rootScope.next = function() {
        for (var i = routers.length - 1; i >= 0; i--) {
            if ($location.path() == routers[i]) {
                $rootScope.breadcrumb[$location.path()] = 'eom-complete';
                $rootScope.updateRouter(routers[i + 1], true);
                $location.path(routers[i + 1]);

                break;
            }
        }
    };
}]);


app.controller('breadcrumbController', ['$scope', '$rootScope',
    function($scope, $rootScope) {

    }
]);


app.controller('homeController', ['$scope', '$rootScope', '$location',
    function($scope, $rootScope, $location) {

        $scope.submit = function() {
            // if logic is ok
            if (true) {
                $rootScope.next();
            }
        };
    }
]);

app.controller('createUserController', ['$scope', '$rootScope', '$location',
    function($scope, $rootScope, $location) {
        // $rootScope.updateRouter($location.path(), true);
        $scope.submit = function() {
            // if logic is ok
            if (true) {
                $rootScope.next();
            }
        };
    }
]);

app.controller('associateHostController', ['$scope', '$rootScope', '$location',
    function($scope, $rootScope, $location) {
        $scope.submit = function() {
            // if logic is ok
            if (true) {
                $rootScope.next();
            }
        };
    }
]);

app.controller('deploymentController', ['$scope', '$rootScope', '$location',
    function($scope, $rootScope, $location) {
        $scope.submit = function() {
            // if logic is ok
            if (true) {
                $rootScope.next();
            }
        };
    }
]);

app.controller('initEnvController', ['$scope', '$rootScope', '$location',
    function($scope, $rootScope, $location) {
        
    }
]);