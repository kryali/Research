
/**
 * Module dependencies.
 */

var express = require('express');

var app = module.exports = express.createServer();

// Configuration

app.configure(function(){
  app.set('views', __dirname + '/views');
  app.set('view engine', 'jade');
  app.use(express.bodyParser());
  app.use(express.methodOverride());
  app.use(app.router);
  app.use(express.static(__dirname + '/public'));
});

app.configure('development', function(){
  app.use(express.errorHandler({ dumpExceptions: true, showStack: true })); 
});

app.configure('production', function(){
  app.use(express.errorHandler()); 
});

hosts = [];
host_id = 0;

// Routes
app.get('/', function(req, res){
  res.render('index', {
    title: 'Express'
  });
});

app.get('/clients', function(req, res) {
  res.render('index', {
    title: 'All Clients'
  });
});

app.get('/hosts/new', function(req, res) {
  console.log( "Received new host" );
  host = { 
    id: host_id,
    type: 'CLIENT',
    start_time: 0,
    bandwidth: 100,
    ip: '192.168.1.1',
    port: 8000,
    clients: []
  };
  host_id++;
  hosts.push( host );

  res.render('index', {
    title: 'Added host: ' + host.id
  });
});

app.get('/hosts/', function(req, res) {
  res.render('hosts/index', {
    title: 'AV Hosts',
    hosts: hosts
  });
});

// This should become post soon
app.get('/hosts/:id', function(req, res) {
  var id = req.params.id;
  if( id <= host_id ) {
    var host = hosts[id];
    res.render('hosts/show', {
      title: 'Showing host id ' + req.params.id,
      host: host
    });
  } else {
    res.render('error', {
      title: "Error",
      error: "Couldn't find host id" + req.params.id
    });
  }
});

app.get('/hosts/:id/client/new', function(req, res) {
  var id = req.params.id;
  // TODO: check to see id is within bounds
  var host = hosts[id];
  var client = { 
    id: 0,
    type: 'CLIENT',
    start_time: 0,
    bandwidth: 100,
    ip: '192.168.1.1',
    port: 8000,
  };
  host.sources.push(client);
});

// Only listen on $ node app.js

if (!module.parent) {
  app.listen(3000);
  console.log("Express server listening on port %d", app.address().port);
}
