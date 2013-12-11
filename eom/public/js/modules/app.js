/**
 * 定义公用函数
 */
/**
 * 文本表单blur事件验证的实际执行函数
 * 
 * @param {[Object]}
 *            target [controller函数中的表单对象]
 * @param {[Boolean]}
 *            valid [该表单的$valid属性]
 * @param {[Object]}
 *            error [该表单的$error属性]
 * @return {[type]} [description]
 */

function verifyTextInputBlur(target, valid, error) {
	if (!target.verify) {
		if (!error.required) { // 内容不为空
			target.verify = true;
			if (valid) {
				target.validity = true;
				target.parent.addClass('verifyGood');
			} else if (error.pattern) { // 如果验证不通过
				target.mismatch = true;
				target.parent.addClass('verifyShit');
			}
		}
	}
	verifyTextInputKeyup(target, valid, error);
}

/**
 * 文本表单Keyup事件验证的实际执行函数
 * 
 * @param {[Object]}
 *            target [controller函数中的表单对象]
 * @param {[Boolean]}
 *            valid [该表单的$valid属性]
 * @param {[Object]}
 *            error [该表单的$error属性]
 * @return {[type]} [description]
 */

function verifyTextInputKeyup(target, valid, error) {
	if (target.verify) {
		if (valid) {
			target.validity = true;
			target.blank = false;
			target.mismatch = false;
			target.parent.addClass('verifyGood');
			target.parent.removeClass('verifyShit');
		} else if (error.required) {
			target.validity = false;
			target.blank = true;
			target.mismatch = false;
			target.parent.addClass('verifyShit');
			target.parent.removeClass('verifyGood');
		} else if (error.pattern) {
			target.validity = false;
			target.blank = false;
			target.mismatch = true;
			target.parent.addClass('verifyShit');
			target.parent.removeClass('verifyGood');
		}
	}
}

/**
 * 文本表单Keyup事件验证的实际执行函数
 * 
 * @param {[Object]}
 *            target [controller函数中的表单对象]
 * @param {[Object]}
 *            error [该表单的$error属性]
 * @param {[Object]}
 *            cmd [该确认密码表单对应的密码表单对象]
 * @return {[type]} [description]
 */

function verifyCmdEqually(target, error, cmd) {
	if (cmd.val) {
		if (error.required) {
			target.validity = false;
			target.blank = true;
			target.equally = false;
			target.parent.addClass('verifyShit');
			target.parent.removeClass('verifyGood');
		} else if (target.val !== cmd.val) {
			target.validity = false;
			target.blank = false;
			target.equally = true;
			target.parent.addClass('verifyShit');
			target.parent.removeClass('verifyGood');
		} else {
			target.validity = true;
			target.blank = false;
			target.equally = false;
			target.parent.addClass('verifyGood');
			target.parent.removeClass('verifyShit');
		}
	}
}

/**
 * 验证下拉框
 * 
 * @param {[Object]}
 *            target [controller函数中的表单对象]
 * @param {[Boolean]}
 *            valid [该表单的$valid属性]
 * @return {[type]} [description]
 */

function verifySelect(target, valid) {
	if (valid) {
		target.validity = true;
		target.blank = false;
		target.parent.addClass('verifyGood');
		target.parent.removeClass('verifyShit');
	} else {
		target.validity = false;
		target.blank = true;
		target.parent.addClass('verifyShit');
		target.parent.removeClass('verifyGood');
	}
}

/**
 * 创建ip, field, username, command等对象
 * 
 * @param {[type]}
 *            data [description]
 * @return {[type]} [description]
 */

function createDataObj(data) {
	var obj = {
		id : data.id,
		val : data.val || '',
		parent : data.parent || $('#' + data.id).closest('.form-group'),
		re : data.re || '',
		showChar : data.showChar || false,
		blank : false, // 表单是否为空，空 == true
		mismatch : false, // 是否和正则表达式匹配，不匹配 == true
		validity : false, // 表单是否已通过验证，通过 == true
		verify : false, // 是否开启验证，开启 == true

		blur : data.blur || '',
		keyup : data.keyup || '',
		change : data.change || ''
	};

	return obj;
}
/*
 * 这些数据都是伪造数据
 */
var databases = [ {
	id : '23',
	ip : '127.0.0.1',
	port : '8080',
	user : 'fujiejie',
	command : '12341234'
}, {
	id : '23',
	ip : '127.0.0.1',
	port : '8080',
	user : 'fujiejie',
	command : '12341234'
}, {
	id : '23',
	ip : '127.0.0.1',
	port : '8080',
	user : 'fujiejie',
	command : '12341234'
}, {
	id : '23',
	ip : '127.0.0.1',
	port : '8080',
	user : 'fujiejie',
	command : '12341234'
}, {
	id : '23',
	ip : '127.0.0.1',
	port : '8080',
	user : 'fujiejie',
	command : '12341234'
}, {
	id : '23',
	ip : '127.0.0.1',
	port : '8080',
	user : 'fujiejie',
	command : '12341234'
}, {
	id : '23',
	ip : '127.0.0.1',
	port : '8080',
	user : 'fujiejie',
	command : '12341234'
}, {
	id : '23',
	ip : '127.0.0.1',
	port : '8080',
	user : 'fujiejie',
	command : '12341234'
}, {
	id : '23',
	ip : '127.0.0.1',
	port : '8080',
	user : 'fujiejie',
	command : '12341234'
}, {
	id : '23',
	ip : '127.0.0.1',
	port : '8080',
	user : 'fujiejie',
	command : '12341234'
}, {
	id : '23',
	ip : '127.0.0.1',
	port : '8080',
	user : 'fujiejie',
	command : '12341234'
} ];
var servers = [ {
	id : '23',
	ip : '127.0.0.1',
	port : '8080',
	field : 'val1',
	user : 'fujiejie',
	command : '12341234'
}, {
	id : '23',
	ip : '127.0.0.1',
	port : '8080',
	field : 'val2',
	user : 'fujiejie',
	command : '12341234'
}, {
	id : '23',
	ip : '127.0.0.1',
	port : '8080',
	field : 'val3',
	user : 'fujiejie',
	command : '12341234'
}, {
	id : '23',
	ip : '127.0.0.1',
	port : '8080',
	field : 'val4',
	user : 'fujiejie',
	command : '12341234'
}, {
	id : '23',
	ip : '127.0.0.1',
	port : '8080',
	field : 'val3',
	user : 'fujiejie',
	command : '12341234'
}, {
	id : '23',
	ip : '127.0.0.1',
	port : '8080',
	field : 'val4',
	user : 'fujiejie',
	command : '12341234'
}, {
	id : '23',
	ip : '127.0.0.1',
	port : '8080',
	field : 'val2',
	user : 'fujiejie',
	command : '12341234'
}, {
	id : '23',
	ip : '127.0.0.1',
	port : '8080',
	field : 'val4',
	user : 'fujiejie',
	command : '12341234'
}, {
	id : '23',
	ip : '127.0.0.1',
	port : '8080',
	field : 'val2',
	user : 'fujiejie',
	command : '12341234'
}, {
	id : '23',
	ip : '127.0.0.1',
	port : '8080',
	field : 'val3',
	user : 'fujiejie',
	command : '12341234'
}, {
	id : '23',
	ip : '127.0.0.1',
	port : '8080',
	field : 'val1',
	user : 'fujiejie',
	command : '12341234'
} ];

var app = angular.module('eomApp', [ 'ngRoute' ]);
var routers = [ '/home', '/setDatabase', '/setWebServer', '/deployConfig', '/deployServer',
		'/initScript', '/initEnv' ];

app
		.config([ '$routeProvider', function($routeProvider) {
			$routeProvider.when('/home', {
				controller : 'homeController',
				templateUrl : '/templates/home.html'
			}).when('/setDatabase', {
				controller : 'setDatabaseCtrl',
				templateUrl : 'templates/setDatabase.html'
			}).when('/setWebServer', {
				controller : 'setWebServerCtrl',
				templateUrl : 'templates/setWebServer.html'
			}).when('/deployConfig', {
				controller : 'deployConfigCtrl',
				templateUrl : 'templates/deployConfig.html'
			}).when('/deployServer', {
				controller : 'deployServerCtrl',
				templateUrl : 'templates/deployServer.html'
			}).when('/initScript', {
				controller : 'initScriptCtrl',
				templateUrl : 'templates/initScript.html'
			}).when('/initEnv', {
				controller : 'initEnvController',
				templateUrl : '/templates/initEnv.html'
			}).otherwise({
				redirectTo : '/home'
			});
		} ])
		.run(
				[
						'$rootScope',
						'$location',
						'$http',
						function($rootScope, $location, $http) {
							$rootScope.breadcrumb = {};

							$rootScope.$on('$locationChangeStart', function(scope, next, current) {
								var currentRouter = $location.path();
								if (currentRouter
										&& !$rootScope.checkRouterAvailable(currentRouter)) {
									scope.preventDefault();
								}
							});

							$rootScope.$on('$locationChangeSuccess',
									function(event, newUrl, oldUrl) {
									});

							$rootScope.updateRouter = function(path, available) {
								$rootScope[path] = typeof available === 'undefined' ? false : true;
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
							$rootScope.prev = function() {
								for (var i = routers.length - 1; i >= 0; i--) {
									if ($location.path() == routers[i]) {
										$rootScope.breadcrumb[$location.path()] = 'eom-complete';
										$rootScope.updateRouter(routers[i - 1], true);
										$location.path(routers[i - 1]);

										break;
									}
								}
							};

							// 域选择列表
							$http.get("/install/allDomains").success(function(res) {
								if (res.result === "success") {
									var domains = res.data;
									var fieldList = [];
									for (var i = 0; i < domains.length; i++) {
										fieldList[i] = {
											label : domains[i].title,
											value : domains[i].id
										};
									}
									$rootScope.fieldList = fieldList;
								}
							});

							$http.get('/install/configs?step=0').success(function(data) {
								if (data.result === 'success') {
									var hosts = data.data;
									for (var i = 0; i < hosts.length; i++) {
										var strdomain = "";
										for (var j = 0; j < hosts[i].domains.length; j++) {
											if (typeof hosts[i].domains[j] === "object") {
												if (j === 0) {
													strdomain += hosts[i].domains[j].title;
												} else {
													strdomain += ";" + hosts[i].domains[j].title;
												}
											} else {
												if (j === 0) {
													strdomain += hosts[i].domains[j];
												} else {
													strdomain += ";" + hosts[i].domains[j];
												}
											}

										}
										hosts[i].strdomain = strdomain;
									}
									$rootScope.hosts = hosts;
								}
							});

							// 接收日志信息，
							$rootScope.infos = [];
							var $logInfo = $('.log-info');

							var socket = io.connect('http://' + location.host);
							socket.on('hello', function(data) {								
								var html = ''
								html += 'console has been connected!';
								$logInfo.append(html).closest('.panel-body').scrollTop(
										$logInfo.outerHeight() + 2000);

							});
							socket.on('info', function(data) {
								var html = '<p class="nomal"><b>[info]</b>' + data.message + '</p>';
								$logInfo.append(html).closest('.panel-body').scrollTop(
										$logInfo.outerHeight() + 2000);
							});
							socket.on('error', function(data) {
								var html = '<p class="error"><b>[error]</b>' + data.message + '</p>';
								$logInfo.append(html).closest('.panel-body').scrollTop(
										$logInfo.outerHeight() + 2000);
							});
							/*
							 * setInterval(function() {
							 * $http.get('/data/log.json').success(
							 * function(data) { var html = '', item; if
							 * (data.result === 'success') { if
							 * ($logInfo.length) { for (var
							 * i=0,len=data.data.length; i<len; i++) { item =
							 * data.data[i]; html += '<p class="'+ item.type +'"><b>['+
							 * item.type +']</b>'+ item.value +'</p>'; }
							 * $logInfo.append(html)
							 * .closest('.panel-body').scrollTop($logInfo.outerHeight() +
							 * 2000); html = ''; } else { $logInfo =
							 * $('.log-info'); } } }); }, 500);
							 * 
							 */
							// 清除日志
							$rootScope.clearLog = function() {
								$logInfo.html('');
							};

							$rootScope.databases = databases; // 数据库列表
							$rootScope.servers = servers; // web服务器列表
							$http.get("/install/sysConfig").success(function(res) {
								var config_user = "";
								var config_ps = "";
								if (res.result === "success") {
									var config = res.data;
									if (config.user) {
										config_user = config.user;
									}
									if (config.password) {
										config_ps = config.password;
									}
								}
								// 设置统一用户
								$rootScope.unifyUser = createDataObj({
									id : 'unifyUserUser',
									val : config_user,
									re : /^[\w]{3,10}$/,
									blur : function(valid, error) {
										this.parent = $('#' + this.id).closest('.form-group');
										verifyTextInputBlur(this, valid, error);
									},
									keyup : function(valid, error) {
										this.parent = $('#' + this.id).closest('.form-group');
										verifyTextInputKeyup(this, valid, error);
									}
								});
								$rootScope.unifyCmd = createDataObj({
									id : 'unifyUserCmd',
									val : config_ps,
									showChar : false,
									re : /^[0-9A-Za-z]{6,20}$/,
									blur : function(valid, error) {
										this.parent = $('#' + this.id).closest('.form-group');
										verifyTextInputBlur(this, valid, error);
									},
									keyup : function(valid, error) {
										this.parent = $('#' + this.id).closest('.form-group');
										verifyTextInputKeyup(this, valid, error);
									}
								});
							});

							/**
							 * 点击提交，会验证表单，如果验证通过则保存表单，否则会提示错误
							 * 
							 * @param {[type]}
							 *            modal [description]
							 * @return {[type]} [description]
							 */
							$rootScope.submitUnifyUser = function(modal) {
								var ok = true, user = $rootScope.unifyUser, cmd = $rootScope.unifyCmd;
								if (!user.validity) {
									ok = false;
									if (user.val === '') {
										verifyTextInputBlur(user, false, {
											required : true,
											pattern : false
										});
									} else {
										verifyTextInputBlur(user, false, {
											required : false,
											pattern : true
										});
									}
								}
								if (!cmd.validity) {
									var ok = false;
									if (cmd.val === '') {
										verifyTextInputBlur(cmd, false, {
											required : true,
											pattern : false
										});
									} else {
										verifyTextInputBlur(cmd, false, {
											required : false,
											pattern : true
										});
									}
								}
								if (ok) {
									$http({
										method : "post",
										url : "/install/saveSysConfig",
										data : {
											config : {
												user : user.val,
												password : cmd.val
											}
										}
									}).success(function(data, status) {
										$rootScope.canUseUnifyUser = true;
										$rootScope.useUnifyUser = true;
										$rootScope.unifyUserVal = user.val;
										$rootScope.unifyCmdVal = cmd.val;
										$(modal).modal('hide');
									}).error(function(data, status) {

									});

								}
							};
							/**
							 * 如果点击了取消或x则，不应该保存结果
							 * 
							 * @param {[type]}
							 *            modal [description]
							 * @return {[type]} [description]
							 */
							$rootScope.resetUnifyUser = function(modal) {
								var user = $rootScope.unifyUser, cmd = $rootScope.unifyCmd;
								verifyTextInputBlur(user, true, {
									required : false,
									pattern : false
								});
								verifyTextInputBlur(cmd, true, {
									required : false,
									pattern : false
								});
								user.val = $rootScope.unifyUserVal;
								cmd.val = $rootScope.unifyCmdVal;
							};
						} ])
		.filter(
				'range',
				function() { // 分页
					return function(input, args) {
						var len = input.length, page = args[0], pagesize = args[1], start = (page - 1)
								* pagesize, end = start + pagesize, output;
						if (end < len) {
							output = input.slice(start, end);
						} else {
							output = input.slice(start);
						}
						return output;
					};
				})
		.directive(
				'pagingbar',
				[ function() {

					function pagesArray(pageNum) {
						var pages = [];
						for (var i = 1; i <= pageNum; i++) {
							pages.push(i);
						}
						return pages;
					}

					var linker = function($scope, element, attrs) {
						var options = $scope.$eval(attrs.pagingbar);
						$scope.active = 0;

						$scope.$parent.$watch(options.data + '.length', function(len) {
							$scope.pages = pagesArray(Math.ceil(len / options.pageSize));
						});

						$scope.pageTo = function($index) {
							$scope.active = $index;
							$index++;
							$scope.$parent.$eval(options.pageTo)($index);
						};

						$scope.prev = function() {
							var index = $scope.active - 1;
							if (index < 0) {
								return;
							}
							$scope.pageTo(index);
						};

						$scope.next = function() {
							var index = $scope.active + 1;
							if (index > ($scope.pages.length - 1)) {
								return;
							}
							$scope.pageTo(index);
						};
					};

					return {
						scope : true,
						link : linker,
						template : '<ul class ="pagination pull-right">'
								+ '   <li class="{{(active === 0) ? \'disabled\' : \'\'}}"><span  ng-click="prev()">&laquo;</span></li>'
								+ '   <li  ng-repeat="p in pages" ng-click="pageTo($index)"'
								+ '        class="{{($index == active)? \'active\': \'\'}}"><span>{{p}}</span></li>'
								+ '   <li class="{{(active === (pages.length-1)) ? \'disabled\' : \'\'}}"><span ng-click="next()">&raquo;</span></li>'
								+ '</ul>'
					};
				} ]);

app.controller('breadcrumbController', [ '$scope', '$rootScope', function($scope, $rootScope) {

} ]);

app
		.controller(
				'homeController',
				[
						'$scope',
						'$rootScope',
						'$location',
						'$http',
						function($scope, $rootScope, $location, $http) {
							$scope.editedIndex = 0; // 保存当前被修改主机的索引
							$scope.page = 1;
							$scope.hostPag = {
								pageSize : 10,
								data : 'hosts',
								pageTo : 'pageTo'
							};
							$scope.pageTo = function(index) {
								$scope.page = index;
							};
							// 设置主机
							$scope.addIp = createDataObj({
								id : 'addHostIp',
								// 验证ip, 具体写法老大再看看
								re : /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/,
								blur : function(valid, error) {
									verifyTextInputBlur(this, valid, error);
								},
								keyup : function(valid, error) {
									verifyTextInputKeyup(this, valid, error);
								}
							});
							$scope.addField = createDataObj({
								id : 'addHostField',
								change : function(valid) {
									verifySelect(this, valid);
								},
								blur : function(valid) {
									verifySelect(this, valid);
								}
							});
							$scope.addUser = createDataObj({
								id : 'addHostUser',
								re : /^[\w]{3,10}$/,
								blur : function(valid, error) {
									verifyTextInputBlur(this, valid, error);
								},
								keyup : function(valid, error) {
									verifyTextInputKeyup(this, valid, error);
								}
							});
							$scope.addCmd = createDataObj({
								val : 'aaa',
								id : 'addHostCmd',
								re : /^[0-9A-Za-z]{6,20}$/,
								showChar : false,
								blur : function(valid, error) {
									verifyTextInputBlur(this, valid, error);
								},
								keyup : function(valid, error) {
									verifyTextInputKeyup(this, valid, error);
								}
							});

							// 设置编辑主机
							$scope.editIp = createDataObj({
								id : 'editHostIp',
								// 验证ip, 具体写法老大再看看
								re : /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/,
								blur : function(valid, error) {
									verifyTextInputBlur(this, valid, error);
								},
								keyup : function(valid, error) {
									verifyTextInputKeyup(this, valid, error);
								}
							});
							$scope.editField = createDataObj({
								id : 'editHostField',
								change : function(valid) {
									verifySelect(this, valid);
								},
								blur : function(valid) {
									verifySelect(this, valid);
								}
							});
							$scope.editUser = createDataObj({
								id : 'editHostUser',
								re : /^[\w]{3,10}$/,
								blur : function(valid, error) {
									verifyTextInputBlur(this, valid, error);
								},
								keyup : function(valid, error) {
									verifyTextInputKeyup(this, valid, error);
								}
							});
							$scope.editCmd = createDataObj({
								id : 'editHostCmd',
								re : /^[0-9A-Za-z]{6,20}$/,
								showChar : false,
								blur : function(valid, error) {
									verifyTextInputBlur(this, valid, error);
								},
								keyup : function(valid, error) {
									verifyTextInputKeyup(this, valid, error);
								}
							});

							/**
							 * 提交主机按钮，会将新增的主机添加到hosts数组中，并且会自动刷新主机列表
							 * 
							 * @param {[type]}
							 *            modal [description]
							 * @return {[type]} [description]
							 */
							$scope.submitHost = function(modal) {
								var ok = true, ip = $scope.addIp, field = $scope.addField, user = $scope.addUser, cmd = $scope.addCmd;
								var useUnifyUser = this.useUnifyUser;
								if (!ip.validity) { // 如果ip验证不通过
									ok = false;
									if (ip.val === '') {
										verifyTextInputBlur(ip, false, {
											required : true,
											pattern : false
										});
									} else {
										verifyTextInputBlur(ip, false, {
											required : false,
											pattern : true
										});
									}
								}
								if (!field.validity) { // 如果field验证不通过
									ok = false;
									verifySelect(field, false);
								}
								if (this.useUnifyUser) {

								} else {
									if (!user.validity) {
										ok = false;
										if (user.val === '') {
											verifyTextInputBlur(user, false, {
												required : true,
												pattern : false
											});
										} else {
											verifyTextInputBlur(user, false, {
												required : false,
												pattern : true
											});
										}
									}
									if (!cmd.validity) {
										if (cmd.val === '') {
											verifyTextInputBlur(cmd, false, {
												required : true,
												pattern : false
											});
										} else {
											verifyTextInputBlur(cmd, false, {
												required : false,
												pattern : true
											});
										}
									}
								}
								if (ok) {

									$http({
										method : "post",
										url : "/install/addConfig",
										data : {
											step : 0,
											config : {
												id : ip.val,
												address : ip.val,
												domains : field.val,
												globalUser : useUnifyUser,
												user : user.val,
												password : cmd.val
											}
										}
									}).success(function(data, status) {
										$(modal).modal('hide');
										// $rootScope.hosts = data.data;
									}).error(function(data, status) {

									});

								}
							};
							// 点击添加按钮的时候重置表单
							$scope.resetHost = function() {
								var ip = $scope.addIp, field = $scope.addField, user = $scope.addUser, cmd = $scope.addCmd;

								$('#addHostForm').find('.verifyShit').removeClass('verifyShit');
								$('#addHostForm').find('.verifyGood').removeClass('verifyGood');

								ip.val = ''; // 重置IP的值
								ip.blank = false;
								ip.mismatch = false;
								ip.validity = false;
								ip.verify = false;

								field.val = ''; // 重置field的值
								field.blank = false;
								field.validity = false;

								user.val = ''; // 重置user的值
								user.blank = false;
								user.mismatch = false;
								user.validity = false;
								user.verify = false;

								cmd.val = ''; // 重置command的值
								cmd.blank = false;
								cmd.mismatch = false;
								cmd.validity = false;
								cmd.verify = false;
							};

							/**
							 * 编辑主机
							 * 
							 * @param {[type]}
							 *            index [description]
							 * @return {[type]} [description]
							 */
							$scope.editHost = function(modal, index) {
								var ip = $scope.editIp, field = $scope.editField, user = $scope.editUser, cmd = $scope.editCmd, host = $scope.hosts[index];

								$scope.editedIndex = index; // 保存当前被修改主机的索引
								$('#editHostForm').find('.verifyShit').removeClass('verifyShit');
								$('#editHostForm').find('.verifyGood').removeClass('verifyGood');
								ip.val = host.id; // 将主机的ip传递到表单
								field.val = host.domains; // 将主机的field传递到表单
								user.val = host.user; // 将主机的user传递到表单
								cmd.val = host.password; // 将主机的command传递到表单
								// 为了实现提交时代验证, 初始化validity为true
								ip.validity = true;
								field.validity = true;
								user.validity = true;
								cmd.validity = true;

								$(modal).modal('show');
							};

							$scope.checkHost = function(index) {
								var host = $scope.hosts[index];
								$http({
									method : "post",
									url : "/install/checkConfig",
									data : {
										step : 0,
										id : host.id
									}
								}).success(function(data, status) {

									$(modal).modal('hide');
								}).error(function(data, status) {

								});
							};
							/**
							 * 保存修改主机信息
							 * 
							 * @param {[type]}
							 *            modal [description]
							 * @return {[type]} [description]
							 */
							$scope.submitEdit = function(modal) {
								var ok = true, ip = $scope.editIp, field = $scope.editField, user = $scope.editUser, cmd = $scope.editCmd, host = $rootScope.hosts[$scope.editedIndex];

								if (ip.validity === true && field.validity === true) {
									if (this.useUnifyUser) {
										host.ip = ip.val;
										host.field = field.val;
										host.user = $rootScope.unifyUserVal;
										host.command = $rootScope.unifyCmdVal;
										$(modal).modal('hide');
									} else if (user.validity === true || cmd.validity === true) {
										host.ip = ip.val;
										host.field = field.val;
										host.user = user.val;
										host.command = cmd.val;
										$(modal).modal('hide');
									}
								}
							};
							/**
							 * 删除主机
							 * 
							 * @param {[type]}
							 *            index [description]
							 * @return {[type]} [description]
							 */
							$scope.deleteHost = function(modal, index) {
								$scope.deleteHostIndex = index;
								$(modal).modal('show');
							};
							$scope.doDelete = function(modal) {
								$(modal).modal('hide');
								var host = $rootScope.hosts[$scope.deleteHostIndex];
								$http({
									method : "post",
									url : "/install/deleteConfig",
									data : {
										step : 0,
										id : host.id
									}
								}).success(function(data, status) {

								}).error(function(data, status) {

								});
							}

							// 设置统一用户

							$scope.nextStep = function() {
								// if logic is ok
								if (true) {
									$rootScope.next();
								}
							};
						} ]);

app
		.controller(
				'setDatabaseCtrl',
				[
						'$scope',
						'$rootScope',
						'$location',
						function($scope, $rootScope, $location) {
							$scope.page = 1;
							$scope.databasePag = {
								pageSize : 10,
								data : 'databases',
								pageTo : 'pageTo'
							};
							$scope.pageTo = function(index) {
								$scope.page = index;
							};

							// 设置数据库
							$scope.addId = createDataObj({
								id : 'addDatabaseId',
								re : /^\d{1,10}$/,
								blur : function(valid, error) {
									verifyTextInputBlur(this, valid, error);
								},
								keyup : function(valid, error) {
									verifyTextInputKeyup(this, valid, error);
								}
							});
							$scope.addIp = createDataObj({
								id : 'addDatabaseIp',
								// 验证ip, 具体写法老大再看看
								re : /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/,
								blur : function(valid, error) {
									verifyTextInputBlur(this, valid, error);
								},
								keyup : function(valid, error) {
									verifyTextInputKeyup(this, valid, error);
								}
							});
							$scope.addPort = createDataObj({
								id : 'addDatabasePort',
								re : /^\d{1,4}$/,
								blur : function(valid, error) {
									verifyTextInputBlur(this, valid, error);
								},
								keyup : function(valid, error) {
									verifyTextInputKeyup(this, valid, error);
								}
							});
							$scope.addUser = createDataObj({
								id : 'addDatabaseUser',
								re : /^[\w]{3,10}$/,
								blur : function(valid, error) {
									verifyTextInputBlur(this, valid, error);
								},
								keyup : function(valid, error) {
									verifyTextInputKeyup(this, valid, error);
								}
							});
							$scope.addCmd = createDataObj({
								id : 'addDatabaseCmd',
								re : /^[0-9A-Za-z]{6,20}$/,
								showChar : false,
								blur : function(valid, error) {
									verifyTextInputBlur(this, valid, error);
								},
								keyup : function(valid, error) {
									verifyTextInputKeyup(this, valid, error);
								}
							});

							// 设置编辑数据库
							$scope.editId = createDataObj({
								id : 'editDatabaseId',
								re : /^\d{1,10}$/,
								blur : function(valid, error) {
									verifyTextInputBlur(this, valid, error);
								},
								keyup : function(valid, error) {
									verifyTextInputKeyup(this, valid, error);
								}
							});
							$scope.editIp = createDataObj({
								id : 'editDatabaseIp',
								// 验证ip, 具体写法老大再看看
								re : /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/,
								blur : function(valid, error) {
									verifyTextInputBlur(this, valid, error);
								},
								keyup : function(valid, error) {
									verifyTextInputKeyup(this, valid, error);
								}
							});
							$scope.editPort = createDataObj({
								id : 'editDatabasePort',
								re : /^\d{1,4}$/,
								blur : function(valid, error) {
									verifyTextInputBlur(this, valid, error);
								},
								keyup : function(valid, error) {
									verifyTextInputKeyup(this, valid, error);
								}
							});
							$scope.editUser = createDataObj({
								id : 'editDatabaseUser',
								re : /^[\w]{3,10}$/,
								blur : function(valid, error) {
									verifyTextInputBlur(this, valid, error);
								},
								keyup : function(valid, error) {
									verifyTextInputKeyup(this, valid, error);
								}
							});
							$scope.editCmd = createDataObj({
								id : 'editDatabaseCmd',
								re : /^[0-9A-Za-z]{6,20}$/,
								showChar : false,
								blur : function(valid, error) {
									verifyTextInputBlur(this, valid, error);
								},
								keyup : function(valid, error) {
									verifyTextInputKeyup(this, valid, error);
								}
							});

							/**
							 * 提交主机按钮，会将新增的主机添加到hosts数组中，并且会自动刷新主机列表
							 * 
							 * @param {[type]}
							 *            modal [description]
							 * @return {[type]} [description]
							 */
							$scope.submitDatabase = function(modal) {
								var ok = true, id = $scope.addId, ip = $scope.addIp, port = $scope.addPort, user = $scope.addUser, cmd = $scope.addCmd;

								if (!id.validity) { // 如果id验证不通过
									ok = false;
									if (id.val === '') {
										verifyTextInputBlur(id, false, {
											required : true,
											pattern : false
										});
									} else {
										verifyTextInputBlur(id, false, {
											required : false,
											pattern : true
										});
									}
								}

								if (!ip.validity) { // 如果ip验证不通过
									ok = false;
									if (ip.val === '') {
										verifyTextInputBlur(ip, false, {
											required : true,
											pattern : false
										});
									} else {
										verifyTextInputBlur(ip, false, {
											required : false,
											pattern : true
										});
									}
								}
								if (!port.validity) { // 如果port验证不通过
									ok = false;
									if (port.val === '') {
										verifyTextInputBlur(port, false, {
											required : true,
											pattern : false
										});
									} else {
										verifyTextInputBlur(port, false, {
											required : false,
											pattern : true
										});
									}
								}
								if (this.useUnifyUser) {
									if (ok) {
										$rootScope.databases.unshift({
											id : id.val,
											ip : ip.val,
											port : port.val,
											user : $rootScope.unifyUserVal,
											command : $rootScope.unifyCmdVal
										});
										$(modal).modal('hide');
									}
								} else {
									if (!user.validity) {
										ok = false;
										if (user.val === '') {
											verifyTextInputBlur(user, false, {
												required : true,
												pattern : false
											});
										} else {
											verifyTextInputBlur(user, false, {
												required : false,
												pattern : true
											});
										}
									}
									if (!cmd.validity) {
										if (cmd.val === '') {
											verifyTextInputBlur(cmd, false, {
												required : true,
												pattern : false
											});
										} else {
											verifyTextInputBlur(cmd, false, {
												required : false,
												pattern : true
											});
										}
									}
									if (ok) {
										$rootScope.databases.unshift({
											id : id.val,
											ip : ip.val,
											port : port.val,
											user : user.val,
											command : cmd.val
										});
										$(modal).modal('hide');
									}
								}
							};
							// 点击添加按钮的时候重置表单
							$scope.resetDatabase = function() {
								var id = $scope.addId, ip = $scope.addIp, port = $scope.addPort, user = $scope.addUser, cmd = $scope.addCmd;

								$('#editDatabaseForm').find('.verifyShit')
										.removeClass('verifyShit');
								$('#editDatabaseForm').find('.verifyGood')
										.removeClass('verifyGood');

								id.val = ''; // 重置Id的值
								id.blank = false;
								id.mismatch = false;
								id.validity = false;
								id.verify = false;

								ip.val = ''; // 重置IP的值
								ip.blank = false;
								ip.mismatch = false;
								ip.validity = false;
								ip.verify = false;

								port.val = ''; // 重置port的值
								port.blank = false;
								port.mismatch = false;
								port.validity = false;
								port.verify = false;

								user.val = ''; // 重置user的值
								user.blank = false;
								user.mismatch = false;
								user.validity = false;
								user.verify = false;

								cmd.val = ''; // 重置command的值
								cmd.blank = false;
								cmd.mismatch = false;
								cmd.validity = false;
								cmd.verify = false;
							};

							/**
							 * 编辑主机
							 * 
							 * @param {[type]}
							 *            index [description]
							 * @return {[type]} [description]
							 */
							$scope.editDatabase = function(modal, index) {
								var id = $scope.editId, ip = $scope.editIp, port = $scope.editPort, user = $scope.editUser, cmd = $scope.editCmd, database = $rootScope.databases[index];

								$scope.editedIndex = index; // 保存当前被修改主机的索引
								$('#editDatabaseForm').find('.verifyShit')
										.removeClass('verifyShit');
								$('#editDatabaseForm').find('.verifyGood')
										.removeClass('verifyGood');
								id.val = database.id; // 将主机的id传递到表单
								ip.val = database.ip; // 将主机的ip传递到表单
								port.val = database.port; // 将主机的field传递到表单
								user.val = database.user; // 将主机的user传递到表单
								cmd.val = database.command; // 将主机的command传递到表单
								// 为了实现提交时代验证, 初始化validity为true
								id.validity = true;
								ip.validity = true;
								port.validity = true;
								user.validity = true;
								cmd.validity = true;

								$(modal).modal('show');
							};
							/**
							 * 保存修改主机信息
							 * 
							 * @param {[type]}
							 *            modal [description]
							 * @return {[type]} [description]
							 */
							$scope.submitEdit = function(modal) {
								var ok = true, id = $scope.editId, ip = $scope.editIp, port = $scope.editPort, user = $scope.editUser, cmd = $scope.editCmd, database = $rootScope.databases[$scope.editedIndex];

								if (id.validity === true && ip.validity === true
										&& port.validity === true) {
									if (this.useUnifyUser) {
										database.id = id.val;
										database.ip = ip.val;
										database.port = port.val;
										database.user = $rootScope.unifyUserVal;
										database.command = $rootScope.unifyCmdVal;
										$(modal).modal('hide');
									} else if (user.validity === true || cmd.validity === true) {
										database.id = id.val;
										database.ip = ip.val;
										database.port = port.val;
										database.user = user.val;
										database.command = cmd.val;
										$(modal).modal('hide');
									}
								}
							};
							/**
							 * 删除主机
							 * 
							 * @param {[type]}
							 *            index [description]
							 * @return {[type]} [description]
							 */
							$scope.deleteDatabase = function(index) {
								$rootScope.databases.splice(index, 1);
							};

							$scope.prevStep = function() {
								// if logic is ok
								if (true) {
									$rootScope.prev();
								}
							};
							$scope.nextStep = function() {
								// if logic is ok
								if (true) {
									$rootScope.next();
								}
							};
						} ]);
app
		.controller(
				'setWebServerCtrl',
				[
						'$scope',
						'$rootScope',
						'$location',
						function($scope, $rootScope, $location) {
							$scope.page = 1;
							$scope.serverPag = {
								pageSize : 10,
								data : 'servers',
								pageTo : 'pageTo'
							};
							$scope.pageTo = function(index) {
								$scope.page = index;
							};

							// 设置web服务器
							$scope.addId = createDataObj({
								id : 'addServerId',
								re : /^\d{1,10}$/,
								blur : function(valid, error) {
									verifyTextInputBlur(this, valid, error);
								},
								keyup : function(valid, error) {
									verifyTextInputKeyup(this, valid, error);
								}
							});
							$scope.addIp = createDataObj({
								id : 'addServerIp',
								// 验证ip, 具体写法老大再看看
								re : /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/,
								blur : function(valid, error) {
									verifyTextInputBlur(this, valid, error);
								},
								keyup : function(valid, error) {
									verifyTextInputKeyup(this, valid, error);
								}
							});
							$scope.addPort = createDataObj({
								id : 'addServerPort',
								re : /^\d{1,4}$/,
								blur : function(valid, error) {
									verifyTextInputBlur(this, valid, error);
								},
								keyup : function(valid, error) {
									verifyTextInputKeyup(this, valid, error);
								}
							});
							$scope.addField = createDataObj({
								id : 'addServerField',
								change : function(valid) {
									verifySelect(this, valid);
								},
								blur : function(valid) {
									verifySelect(this, valid);
								}
							});
							$scope.addUser = createDataObj({
								id : 'addServerUser',
								re : /^[\w]{3,10}$/,
								blur : function(valid, error) {
									verifyTextInputBlur(this, valid, error);
								},
								keyup : function(valid, error) {
									verifyTextInputKeyup(this, valid, error);
								}
							});
							$scope.addCmd = createDataObj({
								id : 'addServerCmd',
								re : /^[0-9A-Za-z]{6,20}$/,
								showChar : false,
								blur : function(valid, error) {
									verifyTextInputBlur(this, valid, error);
								},
								keyup : function(valid, error) {
									verifyTextInputKeyup(this, valid, error);
								}
							});

							// 设置编辑web服务器
							$scope.editId = createDataObj({
								id : 'editServerId',
								re : /^\d{1,10}$/,
								blur : function(valid, error) {
									verifyTextInputBlur(this, valid, error);
								},
								keyup : function(valid, error) {
									verifyTextInputKeyup(this, valid, error);
								}
							});
							$scope.editIp = createDataObj({
								id : 'editServerIp',
								// 验证ip, 具体写法老大再看看
								re : /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/,
								blur : function(valid, error) {
									verifyTextInputBlur(this, valid, error);
								},
								keyup : function(valid, error) {
									verifyTextInputKeyup(this, valid, error);
								}
							});
							$scope.editPort = createDataObj({
								id : 'editServerPort',
								re : /^\d{1,4}$/,
								blur : function(valid, error) {
									verifyTextInputBlur(this, valid, error);
								},
								keyup : function(valid, error) {
									verifyTextInputKeyup(this, valid, error);
								}
							});
							$scope.editField = createDataObj({
								id : 'editServerField',
								change : function(valid) {
									verifySelect(this, valid);
								},
								blur : function(valid) {
									verifySelect(this, valid);
								}
							});
							$scope.editUser = createDataObj({
								id : 'editServerUser',
								re : /^[\w]{3,10}$/,
								blur : function(valid, error) {
									verifyTextInputBlur(this, valid, error);
								},
								keyup : function(valid, error) {
									verifyTextInputKeyup(this, valid, error);
								}
							});
							$scope.editCmd = createDataObj({
								id : 'editDatabaseCmd',
								re : /^[0-9A-Za-z]{6,20}$/,
								showChar : false,
								blur : function(valid, error) {
									verifyTextInputBlur(this, valid, error);
								},
								keyup : function(valid, error) {
									verifyTextInputKeyup(this, valid, error);
								}
							});

							/**
							 * 提交主机按钮，会将新增的主机添加到hosts数组中，并且会自动刷新主机列表
							 * 
							 * @param {[type]}
							 *            modal [description]
							 * @return {[type]} [description]
							 */
							$scope.submitServer = function(modal) {
								var ok = true, id = $scope.addId, ip = $scope.addIp, port = $scope.addPort, field = $scope.addField, user = $scope.addUser, cmd = $scope.addCmd;

								if (!id.validity) { // 如果id验证不通过
									ok = false;
									if (id.val === '') {
										verifyTextInputBlur(id, false, {
											required : true,
											pattern : false
										});
									} else {
										verifyTextInputBlur(id, false, {
											required : false,
											pattern : true
										});
									}
								}
								if (!ip.validity) { // 如果ip验证不通过
									ok = false;
									if (ip.val === '') {
										verifyTextInputBlur(ip, false, {
											required : true,
											pattern : false
										});
									} else {
										verifyTextInputBlur(ip, false, {
											required : false,
											pattern : true
										});
									}
								}
								if (!port.validity) { // 如果port验证不通过
									ok = false;
									if (port.val === '') {
										verifyTextInputBlur(port, false, {
											required : true,
											pattern : false
										});
									} else {
										verifyTextInputBlur(port, false, {
											required : false,
											pattern : true
										});
									}
								}
								if (!field.validity) { // 如果field验证不通过
									ok = false;
									verifySelect(field, false);
								}
								if (this.useUnifyUser) {
									if (ok) {
										$rootScope.servers.unshift({
											id : id.val,
											ip : ip.val,
											port : port.val,
											field : field.val,
											user : $rootScope.unifyUserVal,
											command : $rootScope.unifyCmdVal
										});
										$(modal).modal('hide');
									}
								} else {
									if (!user.validity) {
										ok = false;
										if (user.val === '') {
											verifyTextInputBlur(user, false, {
												required : true,
												pattern : false
											});
										} else {
											verifyTextInputBlur(user, false, {
												required : false,
												pattern : true
											});
										}
									}
									if (!cmd.validity) {
										if (cmd.val === '') {
											verifyTextInputBlur(cmd, false, {
												required : true,
												pattern : false
											});
										} else {
											verifyTextInputBlur(cmd, false, {
												required : false,
												pattern : true
											});
										}
									}
									if (ok) {
										$rootScope.servers.unshift({
											id : id.val,
											ip : ip.val,
											port : port.val,
											field : field.val,
											user : user.val,
											command : cmd.val
										});
										$(modal).modal('hide');
									}
								}
							};
							// 点击添加按钮的时候重置表单
							$scope.resetServer = function() {
								var id = $scope.addId, ip = $scope.addIp, port = $scope.addPort, field = $scope.addField, user = $scope.addUser, cmd = $scope.addCmd;

								$('#editServerForm').find('.verifyShit').removeClass('verifyShit');
								$('#editServerForm').find('.verifyGood').removeClass('verifyGood');

								id.val = ''; // 重置Id的值
								id.blank = false;
								id.mismatch = false;
								id.validity = false;
								id.verify = false;

								ip.val = ''; // 重置IP的值
								ip.blank = false;
								ip.mismatch = false;
								ip.validity = false;
								ip.verify = false;

								port.val = ''; // 重置port的值
								port.blank = false;
								port.mismatch = false;
								port.validity = false;
								port.verify = false;

								field.val = ''; // 重置field的值
								field.blank = false;
								field.verify = false;

								user.val = ''; // 重置user的值
								user.blank = false;
								user.mismatch = false;
								user.validity = false;
								user.verify = false;

								cmd.val = ''; // 重置command的值
								cmd.blank = false;
								cmd.mismatch = false;
								cmd.validity = false;
								cmd.verify = false;
							};

							/**
							 * 编辑主机
							 * 
							 * @param {[type]}
							 *            index [description]
							 * @return {[type]} [description]
							 */
							$scope.editServer = function(modal, index) {
								var id = $scope.editId, ip = $scope.editIp, port = $scope.editPort, field = $scope.editField, user = $scope.editUser, cmd = $scope.editCmd, server = $rootScope.servers[index];

								$scope.editedIndex = index; // 保存当前被修改主机的索引
								$('#editServerForm').find('.verifyShit').removeClass('verifyShit');
								$('#editServerForm').find('.verifyGood').removeClass('verifyGood');
								id.val = server.id; // 将主机的id传递到表单
								ip.val = server.ip; // 将主机的ip传递到表单
								port.val = server.port; // 将主机的field传递到表单
								field.val = server.field; // 将主机的fieldr传递到表单
								user.val = server.user; // 将主机的user传递到表单
								cmd.val = server.command; // 将主机的command传递到表单
								// 为了实现提交时代验证, 初始化validity为true
								id.validity = true;
								ip.validity = true;
								port.validity = true;
								field.validity = true;
								user.validity = true;
								cmd.validity = true;

								$(modal).modal('show');
							};
							/**
							 * 保存修改主机信息
							 * 
							 * @param {[type]}
							 *            modal [description]
							 * @return {[type]} [description]
							 */
							$scope.submitEdit = function(modal) {
								var ok = true, id = $scope.editId, ip = $scope.editIp, port = $scope.editPort, field = $scope.editField, user = $scope.editUser, cmd = $scope.editCmd, server = $rootScope.servers[$scope.editedIndex];

								if (id.validity === true && ip.validity === true
										&& port.validity === true) {
									if (this.useUnifyUser) {
										server.id = id.val;
										server.ip = ip.val;
										server.port = port.val;
										server.field = field.val;
										server.user = $rootScope.unifyUserVal;
										server.command = $rootScope.unifyCmdVal;
										$(modal).modal('hide');
									} else if (user.validity === true || cmd.validity === true) {
										server.id = id.val;
										server.ip = ip.val;
										server.port = port.val;
										server.field = field.val;
										server.user = user.val;
										server.command = cmd.val;
										$(modal).modal('hide');
									}
								}
							};
							/**
							 * 删除主机
							 * 
							 * @param {[type]}
							 *            index [description]
							 * @return {[type]} [description]
							 */
							$scope.deleteServer = function(index) {
								$rootScope.servers.splice(index, 1);
							};

							$scope.prevStep = function() {
								// if logic is ok
								if (true) {
									$rootScope.prev();
								}
							};
							$scope.nextStep = function() {
								// if logic is ok
								if (true) {
									$rootScope.next();
								}
							};
						} ]);

app.controller('deployConfigCtrl', [ '$scope', '$rootScope', '$location',
		function($scope, $rootScope, $location) {

			$scope.prevStep = function() {
				// if logic is ok
				if (true) {
					$rootScope.prev();
				}
			};
			$scope.nextStep = function() {
				// if logic is ok
				if (true) {
					$rootScope.next();
				}
			};
		} ]);

app.controller('deployServerCtrl', [ '$scope', '$rootScope', '$location',
		function($scope, $rootScope, $location) {

			$scope.prevStep = function() {
				// if logic is ok
				if (true) {
					$rootScope.prev();
				}
			};
			$scope.nextStep = function() {
				// if logic is ok
				if (true) {
					$rootScope.next();
				}
			};
		} ]);

app.controller('initScriptCtrl', [ '$scope', '$rootScope', '$location',
		function($scope, $rootScope, $location) {

			$scope.prevStep = function() {
				// if logic is ok
				if (true) {
					$rootScope.prev();
				}
			};
			$scope.nextStep = function() {
				// if logic is ok
				if (true) {
					$rootScope.next();
				}
			};
		} ]);

app.controller('initEnvController', [ '$scope', '$rootScope', '$location',
		function($scope, $rootScope, $location) {

			$scope.prevStep = function() {
				// if logic is ok
				if (true) {
					$rootScope.prev();
				}
			};
		} ]);

var logResize = false, // 是否开启改变尺寸
clientHeight = $('body').height(), // 视窗高度
logHeight, wrapHeight, heightFull, heightMinus;
$(document).on('mousedown.resize', '.log-resize-handler', function(e) {
	logResize = true;
	$('.log-resize-modal, .log-resize-line').show(0);
}).on('mousemove.resize', 'body', function(e) {
	if (logResize) {
		logHeight = clientHeight - e.clientY;
		$('.log-resize-handler').css('bottom', logHeight - 6);
	}
}).on('mouseup.resize', 'body', function(e) {
	logResize = false;
	$('.log-resize-modal, .log-resize-line').hide(0);
	$('.mix-log').height(logHeight);
	$('.mix-wrapper').css('bottom', logHeight);
})

.on('click.resize', '.mix-log .glyphicon-resize-full', function(e) {
	var $tag = $(e.target);
	heightFull = $('.mix-log').height();
	$tag.removeClass('glyphicon-resize-full').addClass('glyphicon-resize-small');
	// 全屏后，加号不应该继续存在，
	$('.mix-log .glyphicon-plus').removeClass('glyphicon-plus').addClass('glyphicon-minus');
	$('.mix-log').height(clientHeight);
}).on('click.resize', '.mix-log .glyphicon-resize-small', function(e) {
	var $tag = $(e.target);
	$tag.removeClass('glyphicon-resize-small').addClass('glyphicon-resize-full');
	$('.mix-log').height(heightFull < clientHeight * 1 / 4 ? clientHeight * 1 / 4 : heightFull);
})

.on(
		'click.resize',
		'.mix-log .glyphicon-minus',
		function(e) {
			var $tag = $(e.target);
			$('.mix-log .panel-body').hide();
			heightMinus = $('.mix-log').height();
			$('.mix-log').height($tag.closest('.panel-heading').outerHeight());
			// 最小化后，全屏还原按钮不应该存在
			$('.mix-log .glyphicon-resize-small').removeClass('glyphicon-resize-small').addClass(
					'glyphicon-resize-full');
			$tag.removeClass('glyphicon-minus').addClass('glyphicon-plus');
		}).on('click.resize', '.mix-log .glyphicon-plus', function(e) {
	var $tag = $(e.target);
	$('.mix-log .panel-body').show();
	$('.mix-log').height(heightMinus > clientHeight * 2 / 3 ? clientHeight * 2 / 3 : heightMinus);
	$tag.removeClass('glyphicon-plus').addClass('glyphicon-minus');
});
