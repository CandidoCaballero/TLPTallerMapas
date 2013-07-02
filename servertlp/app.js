//Load app dependencies  
var express = require('express'),  
  mongoose = require('mongoose'), 
  path = require('path'),  
  http = require('http'),
  util = require('util');

var app = express();  

//Configure: bodyParser to parse JSON data  
//           methodOverride to implement custom HTTP methods  
//           router to crete custom routes  
app.configure(function(){  
  app.set('views', __dirname + '/views');
  app.set('view engine', 'jade');
  app.use(express.favicon());
  app.use(express.logger('dev'));
  app.use(express.bodyParser());
  app.use(express.methodOverride());
  app.use(app.router);
  app.use(express.static(path.join(__dirname, 'public')));
});  
  
app.configure('development', function(){  
  app.use(express.errorHandler());  
});  
  
//Sample routes are in a separate module, just for keep the code clean  
routes = require('./routes/router')(app);  
  
//Connect to the MongoDB test database  
mongoose.connect('mongodb://localhost/tlp');  
  
//Start the server  
http.createServer(app).listen(9001);  