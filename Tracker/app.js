
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

function add_host( hosts, host ) {

  for( var i = 0 ; i < hosts.length; i++) {
    var current_host = hosts[i];
    if( host.id == current_host.id) {
      console.log("Host already exists");
      return;
    }
  }

  hosts.push(host);
}

function find_host( hosts, host_id ) {
  for( var i = 0 ; i < hosts.length; i++) {
    if(hosts[i]['id'] == host_id) {
      return hosts[i];
    }
  }
  return undefined;
}

function find_node( host, node_id ) {
  for( var i = 0 ; i < host.nodes.length; i++) {
    if(host.nodes[i]['id'] == node_id) {
      return host.nodes[i];
    }
  }
  return undefined;
}

function add_node( host, node ) {
  
  var nodes = host.nodes;
  for( var i = 0 ; i < nodes.length; i++) {
    if(nodes[i]['id'] == node.id) {
      /* Node already exists */
      return;
    }
  }
  host.nodes.push(node);
}

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
  var host_id = req.body.id;
  var host = req.body;//{ host_id: req.body };
  host.nodes = [];
  add_host( hosts, host );
  //req.body['start time'] = new Date(req.body['start time']);
  //hosts[req.body.id] = req.body;
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
    res.send( { hosts: hosts } , {'Content-Type':'application/json'}, 200);
  };
});

// This should become post soon
app.get('/hosts/:id', function(req, res) {
  var id = req.params.id;
  var host = find_host( hosts, id );
  if( host != undefined ) {
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

// This should become post soon
app.get('/hosts/:host_id/nodes/:node_id', function(req, res) {
  var host_id = req.params.host_id;
  var node_id = req.params.node_id;
  var host = find_host( hosts, host_id );
  if( host != undefined ) {
    var node = find_node( host, node_id );
    if( node != undefined ) {
      res.render('hosts/show', {
        title: 'Showing node ' + node_id,
        host: node
      });
      return;
    }
  } 

  res.render('error', {
    title: "Error",
    error: "Couldn't find node id" +  node_id
  });
});

app.post('/hosts/:id/client/new', function(req, res) {
  console.log( "Got new client request" );
  var id = req.params.id;
  var host = find_host( hosts, id );
  if( host != undefined ) {
    var node = req.body;
    add_node( host, node );
  }
});

// Only listen on $ node app.js

if (!module.parent) {
  app.listen(20000);
  console.log("Express server listening on port 20000");
}
