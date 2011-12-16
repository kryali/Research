
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

hosts = {};

// Routes
app.get('/', function(req, res){
  res.redirect('/hosts/');
});

app.get('/clients', function(req, res) {
  res.render('index', {
    title: 'All Clients'
  });
});

app.post('/hosts/new', function(req, res) {
  console.log( "Received host post" );
  console.log( req.body );
  hosts[req.body.id] = req.body;
  res.send("Got request");
});

app.get('/hosts/', function(req, res) {
  res.render('hosts/index', {
    title: 'AV Hosts',
    hosts: hosts
  });
});

app.get('/hosts.:format?', function( req, res ) {
  if( req.params.format == 'json' ) {
    console.log( "Json request received");
    res.send( hosts , {'Content-Type':'application/json'}, 200);
  };
});

// This should become post soon
app.get('/hosts/:id', function(req, res) {
  var id = req.params.id;
  if( hosts[id] != undefined ) {
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
  app.listen(20000);
  console.log("Express server listening on port 20000");
}
