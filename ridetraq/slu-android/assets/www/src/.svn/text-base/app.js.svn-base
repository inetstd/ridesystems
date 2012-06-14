$(window).resize(function() { 
  if (RTApp.views && RTApp.views.mapView && RTApp.views.mapView.map) {
    RTApp.fixHeight($("#map"));
    google.maps.event.trigger(RTApp.views.mapView.map, "resize");
  }
});

$(document).click(function( event ) {   

  var link = event.target;
  var btn = $(link).closest( ".ui-btn" );
  $activeClickedLink = btn.not( ".ui-disabled" ).not('.menu');
  $activeClickedLink.addClass( $.mobile.activeBtnClass );  
  if (!btn.hasClass("no-deselect")) {
    setTimeout(function() {
      $activeClickedLink.removeClass( $.mobile.activeBtnClass );  
    }, 200);
  }
  return true;
});

$(document).ready(function(){
  RTApp.load();
  RTApp.dialogs.common = new Dialog("#commonDialog");  
  RTApp.dialogs.routeInfo = new Dialog("#routeInfo");  
});

RTApp = {};

RTApp.cache = true;

RTApp.clearAndReload = function() {
  LocalStorageManager.clear();
  window.location.reload();
}
// constants
RTApp.C_LOCALSTORAGE_CLIENT_ID                   = "RTApp.C_LOCALSTORAGE_CLIENT_ID______________"
RTApp.C_LOCALSTORAGE_CACHE_4_GET_CLIENTS         = "C_LOCALSTORAGE_CAHCE_4_GET_CLIENTS__________" + ((!RTApp.cache) ? Math.random() : "");
RTApp.C_LOCALSTORAGE_CACHE_4_GET_ROUTES          = "C_LOCALSTORAGE_CACHE_4_GET_ROUTES___________" + ((!RTApp.cache) ? Math.random() : "");
RTApp.C_LOCALSTORAGE_CACHE_4_GET_STOPS           = "C_LOCALSTORAGE_CACHE_4_GET_STOPS____________" + ((!RTApp.cache) ? Math.random() : "");
RTApp.C_LOCALSTORAGE_CACHE_4_GET_CLIENT_SETTINGS = "C_LOCALSTORAGE_CACHE_4_GET_CLIENT_SETTINGS__" + ((!RTApp.cache) ? Math.random() : "");
RTApp.C_LOCALSTORAGE_CACHE_4_GET_ROUTE_SCHEDULES = "C_LOCALSTORAGE_CACHE_4_GET_ROUTE_SCHEDULES__" + ((!RTApp.cache) ? Math.random() : "");

// config 
RTApp.cfg = {};
RTApp.cfg.useLocalStorageCache = (typeof(localStorage) != 'undefined');

RTApp.cfg.BUS_REFRESH_TIME = 3 * 1000;
RTApp.cfg.TWITTS_PER_PAGE  = 5;

RTApp.cfg.urls = {};
RTApp.cfg.urls.GET_CLIENTS_URL     = 'http://www.ridesystems.net/RideAdmin/Services/MobileClientService.svc/JSON/GetClients';
RTApp.cfg.urls.GET_STOPS_URL       = 'http://{WebAddress}/Services/JSONPRelay.svc/GetStops';
RTApp.cfg.urls.GET_ROUTES_URL      = 'http://{WebAddress}/Services/JSONPRelay.svc/GetRoutes';
RTApp.cfg.urls.GET_ROUTE_URL       = 'http://{WebAddress}/dev/services/Jsonprelay.svc/GetRoutes'; // BE CAREFULL!!! DEV
RTApp.cfg.urls.GET_BUSES_URL       = 'http://{WebAddress}/Services/JSONPRelay.svc/GetMapVehiclePoints';
RTApp.cfg.urls.GET_CLIENT_SETTINGS = 'http://{WebAddress}/services/Jsonprelay.svc/GetClientSettings';
RTApp.cfg.urls.GET_ESTIMATES_URL   = 'http://{WebAddress}/Services/JSONPRelay.svc/GetMapStopEstimates?RouteID={RouteID}';
RTApp.cfg.urls.GET_ROUTE_SCHEDULES = 'http://{WebAddress}/Services/JSONPRelay.svc/GetRouteSchedules';
RTApp.cfg.urls.TWITTER_URL         = 'https://twitter.com/status/user_timeline/{q}.json?count={rpp}';

RTApp.currentState = {};
RTApp.currentState.client = null;
RTApp.currentState.route  = null;
RTApp.currentState.stops  = null;

RTApp.utils = {};

// namespaces
RTApp.views   = {}; // set of view classes and view instances
RTApp.dialogs = {}; // set of dialog instances
RTApp.model   = {}; // set of model and collection classes


RTApp.load = function() {
  console.log('RTApp.load >>');


  RTApp.router = new RTApp.Router();      
  Backbone.history.start();
  var client = LocalStorageManager.get(RTApp.C_LOCALSTORAGE_CLIENT_ID);  
  if (window.location.hash == "" && client && client.id) {    
    RTApp.router.navigate("map/" + client.id, {trigger: true}); 
  }
  
}

RTApp.goToSelectAgency = function() {
  window.location.href = '#';
}

// router configuration
RTApp.Router = Backbone.Router.extend({
  clientId : false,
  routeId : false,
  routes: { 
    "":                             "landing",    // #help
    "map/:clientId":                "map",        // #search/kiwis
    "map/:clientId/:routeId":       "map",        // #search/kiwis
    "news/:clientId":               "news",       // #search/kiwis
    "arrivals/:clientId":           "arrivals",   // #search/kiwis
    "arrivals/:clientId/:routeId":  "arrivals",   // #search/kiwis
    "routes/:clientId":                 "selectRoute",// #search/kiwis
    "routes/:clientId/:online":         "selectRoute",// #search/kiwis
    "routeSchedule/:clientId/:routeId": "routeSchedule",// #search/kiwis
    "options/:clientId":            "options",     // #search/kiwis/p7
    "404":                          "pageNotFound" // #search/kiwis/p7
  },
  landing: function() {       
    
    if (!RTApp.views.selectAgencyView) RTApp.views.selectAgencyView = new RTApp.views.SelectAgencyView();
    RTApp.views.selectAgencyView.load()    
    this._showView(RTApp.views.selectAgencyView, true);      
  },
  pageNotFound: function() {
    
  },
  map: function(clientId, routeId) {           
    this.clientId = clientId;
    this.routeId = routeId; 
    if (!RTApp.views.mapView) RTApp.views.mapView = new RTApp.views.MapView();    
    RTApp.views.mapView.load(clientId, routeId); 
    this._showView(RTApp.views.mapView);
    

    //google.maps.event.trigger(RTApp.views.mapView.map, "resize");
  },  
  selectRoute: function(clientId, online) {    
    this.clientId = clientId;    
    if (!RTApp.views.selectRouteView) RTApp.views.selectRouteView = new RTApp.views.SelectRouteView();
    RTApp.views.selectRouteView.load(clientId, online);    
    this._showView(RTApp.views.selectRouteView);            
  },
  news: function(clientId) {
    this.clientId = clientId;
    if (!RTApp.views.newsView) RTApp.views.newsView = new RTApp.views.NewsView();
    RTApp.views.newsView.load(clientId);
    this._showView(RTApp.views.newsView);            
  },
  arrivals: function(clientId, routeId) {
    this.clientId = clientId;
    this.routeId = routeId; 
    if (!RTApp.views.arrivalsView) RTApp.views.arrivalsView = new RTApp.views.ArrivalsView();    
    RTApp.views.arrivalsView.load(clientId, routeId);
    this._showView(RTApp.views.arrivalsView);            
    //RTApp.views.ArrivalsView
  },
  options: function(clientId) {    
    if (!RTApp.views.optionsView) RTApp.views.optionsView = new RTApp.views.OptionsView();
    RTApp.setTitle("More");
    RTApp.views.optionsView.load(clientId);
    this._showView(RTApp.views.optionsView);        
  },  
  routeSchedule: function(clientId, routeId) {
    if (!RTApp.views.routeSchedule) RTApp.views.routeSchedule = new RTApp.views.RouteSchedule();    
    RTApp.views.routeSchedule.load(clientId, routeId);
    this._showView(RTApp.views.routeSchedule);
  },
  _showView: function(view) {        
    // fire before hide event of previous view
    if (this.currentView && this.currentView.onBeforeHide) this.currentView.onBeforeHide();

    // fire before show event of new view
    if (view.onBeforeShow) view.onBeforeShow();

    this.currentView = view;
    $(".page").hide(); // hide all previous pages    
    this.currentView.$el.show();              
    if (this.currentView.focus) this.currentView.focus();    
    this.manageMenu();
    setTimeout(function() {      
      

    }, 100);

  },  
  manageMenu: function() {    
    var clientId = RTApp.currentState.client ? RTApp.currentState.client.get('id') : this.clientId;
    var routeId  = RTApp.currentState.route  ? RTApp.currentState.route.get('id')  : this.routeId;
   // if (!clientId) return $("#footer-nav").hide();
    var mapUrl =  '#map/' + clientId;
    var arrivalsUrl = '#arrivals/' + clientId;
    if (routeId) {
        mapUrl += "/" + routeId;  
        arrivalsUrl += "/" + routeId;
    }

    $("#menu-map").attr("href", mapUrl);
    $("#backToMap").attr("href", '#map/' + clientId);

    $("#selectRouteAll").attr("href", '#routes/' + clientId);
    $("#selectRouteOnline").attr("href", '#routes/' + clientId + "/online");

    $("#menu-arrivals").attr("href", arrivalsUrl);
    $("#menu-routes").attr("href", "#routes/" + clientId + "/online");
    $("#menu-news").attr("href", "#news/" + clientId);
    $("#menu-options").attr("href", "#options/" + clientId);

    var matches = window.location.hash.match(/^#([a-zA-Z]+)(\/)?.*/);    
    if (matches && matches[1]) {
      $("#footer-nav a").removeClass("ui-btn-active");
      $("#menu-" + matches[1]).addClass("ui-btn-active");

      $("#footer").show();
    } else {
      $("#footer").hide();
    }
    //ui-btn-active    
  },
  changeView: function(viewId) {
    console.log("changeView to " + viewId);    
    $(viewId).fadeIn();
    if (this.$currentView) {
      this.$currentView.fadeOut('fast', function() {
        $.mobile.hidePageLoadingMsg();
      });
    }
    this.$currentView =  $(viewId);  
    
  }
});


RTApp.setTitle = function(msg) {
  $("#header h1").html(msg);
}

RTApp.fixHeight = function($el) {
  var hh = $("#header").height();
  var fh = $("#footer").height() || 0;  
  //alert('WH' + $(window).height() + " BH" + $('body').height() + " ==  " + ($(window).height() - hh - fh))
  var h = $(window).height() - hh - fh - 5;
  if (h < 310) h = 310; 
  $el.css({minHeight: h});    
  
}
/** VIEWS DEFINITIONS **/

RTApp.views.MapView = Backbone.View.extend({
  el: "#map",
  map: null,
  initialize: function(){
    console.log("MapView - initialize");
    // fix height
    RTApp.fixHeight(this.$el);

    $("#myLocation").click(function() {
      RTApp.views.mapView.goToMyLocation();
      return false;
    });

    _.bindAll(this, 'drawBuses');             
  },
  mapIsLoaded: false,     
  loadMap: function(center, zoom) {
    if (this.mapIsLoaded) {
      setTimeout(function() {
        google.maps.event.trigger(map, "resize");
        RTApp.views.mapView.map.panTo(center);
        if (zoom) RTApp.views.mapView.map.setZoom(zoom);
        $.mobile.hidePageLoadingMsg();
      }, 1000)    
      return;
    }  
    var myOptions = { 
        zoom: (!zoom || zoom < 12) ? zoom: 12, 
        center: center, 
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        trackMarkers: true,
        streetViewControl: false,
        scrollwheel: false,
        zoomControl: true,
        scaleControl: false,
        rotateControl: false,
        panControl: false,
        overviewMapControl: false,
        noClear: true,
        keyboardShortcuts: false,
        mapTypeControl: false,
        draggable: true,
        mapMaker: false,
        disableDoubleClickZoom: false,
        styles: RTApp.mapStyles
    };
    this.map = new google.maps.Map(document.getElementById("map"), myOptions);
    this.mapIsLoaded = true;
    $.mobile.hidePageLoadingMsg();
  },
  goToMyLocation: function() {
    if(!RTApp.LocationTrackerPID){    
      var options = {maximumAge:60000, timeout: 15000, enableHighAccuracy: true};      
      RTApp.LocationTrackerPID = navigator.geolocation.watchPosition(this.onGetLocation, this.onNoLocation, options);
    }  
  },
  onGetLocation: function(position) {    
    if (!RTApp.views.mapView || !RTApp.views.mapView.map) return;    
    var pos = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
    RTApp.views.mapView.map.setCenter(pos);
    if (RTApp.userMarker) {
      RTApp.userMarker.setPosition(pos);
    } else {
      var image = new google.maps.MarkerImage("resources/images/icon_pin.png",
        // This marker is 20 pixels wide by 32 pixels tall.
        new google.maps.Size(48, 48),
        // The origin for this image is 0,0.
        new google.maps.Point(0,0),
        // The anchor for this image is the base of the flagpole at 0,32.
        new google.maps.Point(24, 48),new google.maps.Size(48, 48));
   
      RTApp.userMarker = new google.maps.Marker({
        position: pos,          
        title: "You",
        content: "You",
        map: RTApp.views.mapView.map,
        animation: google.maps.Animation.DROP,
        icon: image,
        zIndex: 10
      });
    }

    if (RTApp.LocationTrackerPID) navigator.geolocation.clearWatch(RTApp.LocationTrackerPID);  
    RTApp.LocationTrackerPID = null;      
  },
  onNoLocation: function(err) {
    if(err.code == 1) {
      alert("Please, enable location tracking");
    } else if( err.code == 2) {
      alert("Please, enable location tracking");
    }
  },
  onBeforeShow: function() {        
    RTApp.model.buses.bind("reset", this.drawBuses, this);
    //$("#map").css({marginTop: document.body.scrollTop})
    //$.mobile.fixedToolbars.show(true);
    $('body').bind('scrollstart', function(e) {
      e.preventDefault();
    });    
    $("#myLocation").show();
  },
  onBeforeHide: function() {    
    this.stopBusPolling();
    $(window.document.body).unbind('scrollstart');
    RTApp.model.buses.unbind("reset", this.drawBuses, this);
    if (RTApp.dialogs.common) RTApp.dialogs.common.close();
    if (RTApp.dialogs.routeInfo) RTApp.dialogs.routeInfo.close();    
    $("#backToMap").hide();
    $("#myLocation").hide();
  },
  load: function(clientId, routeId) {          
    console.log("load map " + clientId + " : " + routeId);            
    $.mobile.showPageLoadingMsg(); // do not forget to hide in render
    
    RTApp.model.clients.fetch({
      success: function() {          
        
        RTApp.currentState.client = RTApp.model.clients.get(clientId); 

        if (!RTApp.currentState.client) return RTApp.goToSelectAgency();

        RTApp.model.fetchClientsSettings(RTApp.currentState.client.get('WebAddress'), 'showstoplabel');        

        RTApp.setTitle(RTApp.currentState.client.get('Name'));

        Canon.charge(2, function() {                                        
          RTApp.views.mapView.render();
          RTApp.views.mapView.drawBuses();
          $(".mask").hide();

          RTApp.views.mapView.startBusPolling();                    
          RTApp.router.manageMenu();

          setTimeout(function() {
            if (routeId) { 
              $("#myLocation").hide();
              $("#backToMap").show();
            } else {
              $("#backToMap").hide();
            }
          }, 1000);       
        })

        
        
          RTApp.model.routes.fetch(RTApp.currentState.client.get("WebAddress"), {
            success : function() {                
              var mapCenterLatLng;    
              var mapZoom;          
              if (routeId) {                
                RTApp.currentState.route = RTApp.model.routes.get(routeId)                
                mapCenterLatLng = new google.maps.LatLng(RTApp.currentState.route.get('MapLatitude'), RTApp.currentState.route.get('MapLongitude'));
                mapZoom = RTApp.currentState.route.get('MapZoom');
                //"<span style='width: 16px; height: 16px; background-color: "+ RTApp.currentState.route.get('MapLineColor') + "' /><span>" + 
                var color1 =  RTApp.currentState.route.get('MapLineColor');
                color2 = '#' + hexDarker(color1, 30);
                RTApp.setTitle('<span style="background-color: '+color1+';  background-image: -webkit-linear-gradient('+color1+', '+color2+');" class="header-label">&nbsp;</span>&nbsp;'+RTApp.currentState.route.get('Description'));
            

              } else {                
                firstRoute = RTApp.model.routes.first();                              
                mapCenterLatLng = new google.maps.LatLng(firstRoute.get('MapLatitude'), firstRoute.get('MapLongitude'));
                mapZoom = firstRoute.get('MapZoom');
                RTApp.currentState.route = false;
              }
              // try to fix iphone bug
              RTApp.views.mapView.loadMap(mapCenterLatLng, mapZoom);
              setTimeout(function () {                
                Canon.tick();
              }, 100);
              
              
            }
          });
                
          RTApp.model.stops.fetch(RTApp.currentState.client.get("WebAddress"), {
            success : function() {
              var jsn = RTApp.model.stops.toJSON();
              RTApp.currentState.stops = [];
              if (routeId) {
                for (var i = 0; i < jsn.length; i++) {
                  if (jsn[i].RouteID == routeId) {
                    RTApp.currentState.stops.push(jsn[i]);
                  }
                }             
              } else {
                RTApp.currentState.stops = false;
              }            
              Canon.tick();
            }
          });                

      }
    });


  }, 
  render: function() {    
    _.each(this.stopMarkers, function(marker,k) {
      marker.setMap(null);
    }, this);    

    if (RTApp.currentState.route && RTApp.currentState.stops) {  
      var setting = RTApp.model.getClientsSettings(RTApp.currentState.client.get('WebAddress'), 'showstoplabel');                
      var toShowLabels;       
      if (!setting) toShowLabels = false; // hack if WS is not ready
      else if (setting.SettingValue == "true") toShowLabels = true;
      else if (setting.SettingValue == "false") toShowLabels = false;

      var coordinates = []
        for (var j = 0; j < RTApp.currentState.stops.length; j++) {
          var stop = RTApp.currentState.stops[j];          
          this.setStopMarkerOnMap(stop, toShowLabels);         
          for (var z = 0; z < stop.MapPoints.length; z++) {
            coordinates.push(new google.maps.LatLng(stop.MapPoints[z].Latitude, stop.MapPoints[z].Longitude));
          }        
        }            
        // if (RTApp.currentState.route.get('RouteID') == 31) RTApp.currentState.route.set('HideRouteLine', true)          

        if (RTApp.currentState.route.get('HideRouteLine') === undefined || !RTApp.currentState.route.get('HideRouteLine')) {
          if (!this.busPath) {
            this.busPath = new google.maps.Polyline({
              path: [],
              strokeColor: RTApp.currentState.route.get('MapLineColor'),
              strokeOpacity: .6,
              strokeWeight: 6,
              zIndex: 10,
              clickable: false
            });
          }  else {
            this.busPath.setOptions({strokeColor: RTApp.currentState.route.get('MapLineColor')});
          }

          this.busPath.setPath(coordinates);      
          this.busPath.setMap(this.map);
        } else {
          if (this.busPath) {
            this.busPath.setMap(null);
            this.busPath = null;
          }    
        } 
    } else {
      if (this.busPath) {
        this.busPath.setMap(null);
        this.busPath = null;
      }
    }
  },    
  setStopMarkerOnMap: function(stop, showlabel) {    
    var sid = "STOP_" + stop.RouteID + "_" + stop.RouteStopID;

     

    var marker = new RouteStopMarker(stop, this.map, showlabel);
    
    google.maps.event.addListener(marker, 'click', function(event) {
      RTApp.dialogs.common.showStop(marker.stop_);
    });

    this.stopMarkers[sid] = marker;         
  },
  setBusMarkerOnMap: function(bus) {

  }, 
  /**
    add new markers,
    move (animete) existing,
    remove off markers
  */
  busMarkers: {},
  stopMarkers: {},
  routePath: null,
  animateMarker: function(marker, bus) {    
    marker.updateBus(bus); 
    marker.show();       
  },
  drawBuses: function() {        
    if (RTApp.model.buses.length == 0) {
      //alert('no buses on map');
    }    
    // remove markers that is on map, but bus goes offline
    _.each(this.busMarkers, function(marker, id) {
      var removeThisMarker = true;      
      RTApp.model.buses.forEach(function (bus) {      
        var bid = "BUS_" + bus.get('VehicleID');
        if (bid == id) {
          removeThisMarker = false;
        }
      }, this);
      if (removeThisMarker) {                        
        marker.hide();
      }
    }, this);
    // add markers or move to new position
    RTApp.model.buses.forEach(function (bus) {       
      var bid = "BUS_" + bus.get('VehicleID');

      var marker = this.busMarkers[bid];
      if (RTApp.currentState.route && bus.get('RouteID') != RTApp.currentState.route.get("RouteID")) {        
        if (marker) {          
          marker.hide();
        }
        return;        
      }       
      if (marker) {
        this.animateMarker(this.busMarkers[bid], bus)
      } else { 
        //overlay = new BusMarker(latlng, RTApp.utils.createBusMarker(bus), this.map);
        marker = new BusMarker(bus, this.map);        
        marker.id = bid;        

        google.maps.event.addListener(marker, 'click', function(event) {
          RTApp.dialogs.common.showBus(marker.bus_);    
        });

        this.busMarkers[bid] = marker;
      }      
    }, this);
  },  
  startBusPolling: function() {
    if (!this.busPollingIntervalID) {     
      var toExec = function() { RTApp.model.buses.fetch(RTApp.currentState.client.get("WebAddress")); }
      toExec(); // first call
      this.busPollingIntervalID = setInterval(toExec, RTApp.cfg.BUS_REFRESH_TIME);    
    }
  },
  stopBusPolling: function(){
    clearInterval(this.busPollingIntervalID);
    this.busPollingIntervalID = null;   
  }
});

RTApp.views.OptionsView = Backbone.View.extend({
  el: "#options",  
  initialize: function(){
    console.log("OptionsView - initialize");    
    this.render();
  },
  onBeforeShow: function() {
    RTApp.fixHeight(this.$el);
  },
  load: function(clientId) {
    $.mobile.showPageLoadingMsg();
    $("#goToGuide").hide();
    RTApp.model.clients.fetch({
      success: function() {        
        RTApp.currentState.client =  RTApp.model.clients.get(clientId);        
        if (!RTApp.currentState.client) return RTApp.goToSelectAgency();          
        RTApp.model.fetchClientsSettings(RTApp.currentState.client.get('WebAddress'), 'userguide', function(val) {          
          if (val && val.SettingValue)  {
            $("#goToGuide").attr("href", val.SettingValue).show();
          } else {
            $("#goToGuide").hide();
          }          
        });

        RTApp.model.fetchClientsSettings(RTApp.currentState.client.get('WebAddress'), 'feedbackemail', function(val) {          
          if (val && val.SettingValue)  {
            $("#appFeedback").attr("href", "mailto:" + val.SettingValue).show();
          } else {
            $("#appFeedback").hide();
          }          
        });

        RTApp.router.manageMenu();
        $("#goToWebSite").attr("href", "http://" + RTApp.currentState.client.get("WebAddress"));
        RTApp.views.optionsView.render();
      }
    });
  },
  render: function(){     
    console.log("OptionsView - render");
    $.mobile.hidePageLoadingMsg();
  }, 
  clearCache: function () {
      LocalStorageManager.clear()
  }, 
  events: {
    'click #clearCache' : 'clearCache'      
  }
});

RTApp.views.SelectAgencyView = Backbone.View.extend({
  el: "#selectAgency",
  template: "<% var lastLetter = 0; _.each(clients, function(client) { var clientLetter = client.get('Name').toUpperCase()[0] %> <% if (lastLetter != clientLetter) { lastLetter = clientLetter; %><li data-role='list-divider'><%= clientLetter %></li><% } %><li><span class='client-li' style='background-color: #<%= client.get('HexColor') %>;'></span><a rel='<%= client.get('ClientID') %>' href='#map/<%= client.get('ClientID') %>'><%= client.get('Name') %></a></li> <% }); %>",
  initialize: function() {
    console.log("SelectAgency - initialize");   
    //RTApp.model.clients = new RTApp.model.Clients();     
    this.model = RTApp.model.clients;
    _.bindAll(this, 'render');            
  },  
  onBeforeShow: function() {            
    //this.model.bind('reset', this.render, this);
  },
  onBeforeHide: function() {    
    //this.model.unbind('reset', this.render, this);
  },
  load: function() {
    RTApp.setTitle("Select Agency");
    this.model.fetch({
      success: function() {
        RTApp.views.selectAgencyView.render();
      }
    }); 
  },
  render: function(){         
    console.log("SelectAgency - render");       

    var alphabetical = this.model.sortBy(function(client) {
      return client.get("Name").toLowerCase();
    });    
    var html = _.template(this.template, {clients: alphabetical} );
    $("ul", this.$el).html(html).listview('refresh');   
    $.mobile.hidePageLoadingMsg(); 
    return this;
  },   
  events: {
    "click a": "selectClient"
  }, 
  selectClient: function(p) {        
    var client = {id: $(p.target).attr("rel")};
    LocalStorageManager.set(RTApp.C_LOCALSTORAGE_CLIENT_ID, client);
  }
});

RTApp.views.SelectRouteView = Backbone.View.extend({
  el: "#selectRoute",  
  template: "<% _.each(routes, function(route) { %> <li><span class='bus-li' style='background-color: <%= route.MapLineColor %>'></span><a rel='<%= route.RouteID %>' href='#map/<%= client.id %>/<%= route.RouteID %>'><%= route.Description %> </a> <% if (route.StopTimesPDFLink && route.StopTimesPDFLink != '') { %> <a data-icon='info' class='pdf' rel='<%= route.StopTimesPDFLink %>'></a><% } %></li> <% }); %>",
  initialize: function() {
    console.log("SelectRouteView - initialize");    
    this.model = RTApp.model.routes;
    _.bindAll(this, 'render')       
  },
  goToPdf: function(e) {
   var url = $(e.currentTarget).attr("rel");
   LocalStorageManager.set("url_of_image", {url: url});
   window.location.href = "img.html";

  },
  events: {
     'click .pdf': 'goToPdf'
  },
  onBeforeShow: function() {    
    RTApp.fixHeight(this.$el);
    //this.model.bind('reset', this.render, this);    
  },
  onBeforeHide: function() {        
    //this.model.unbind('reset', this.render, this);
  },
  onlineMode: false,
  onlineRoutesIds: [],
  load: function(clientId, online) {
    $.mobile.showPageLoadingMsg();
    
    RTApp.model.clients.fetch({
      success: function() {
        RTApp.currentState.client =  RTApp.model.clients.get(clientId);        
        if (!RTApp.currentState.client) return RTApp.goToSelectAgency();        
        RTApp.model.routes.fetch(RTApp.currentState.client.get("WebAddress"), {
          success: function() {
            RTApp.views.selectRouteView.onlineMode = online;
            if (online) {     
              // load estimates to filter online / offline
              RTApp.model.estimates.fetch(RTApp.currentState.client.get("WebAddress"), null, {
                success: function(res) {
                  var routes = res.toJSON();
                  RTApp.views.selectRouteView.onlineRoutesIds = {};                  

                  setTimeout(function() {
                    $.mobile.hidePageLoadingMsg();                    
                    
                      var size = 0;
                      for (var key in RTApp.views.selectRouteView.onlineRoutesIds) {
                        if (RTApp.views.selectRouteView.onlineRoutesIds.hasOwnProperty(key)) size++;
                      }     
                      if (size == 0) {
                        RTApp.flashMessage("No active routes");
                        RTApp.router.navigate("routes/" + clientId, {trigger: true});
                      }
                  
                  }, 3000)

                  RTApp.model.fetchRouteSchedules(RTApp.currentState.client.get("WebAddress"), function (res) {                                      
                    var d = new Date()
                    var n = d.getTimezoneOffset();                    
                    var myOffset =  n / 60;
                    console.log(d.getTime())

                    for (var i in res) {
                      var route = res[i];
                      console.log(route);
                      var matchesStart = route.StartTime.match(/Date\((\d+)([-+])(\d{2})(\d{2})\)/);
                      var matchesEnd   = route.EndTime.match(/Date\((\d+)([-+])(\d{2})(\d{2})\)/);
                      if (matchesStart.length != 5 || matchesEnd.length != 5) continue;
                        if ( matchesStart[3][0] == "0" )  matchesStart[3] = matchesStart[3].substring(1);
                       
                         var offsetToGreenwich = (parseInt(matchesStart[3]));                        
                         if (matchesStart[2] == "-") {
                           offsetToGreenwich *= -1;
                         }
                        
                        var startTmz =  parseInt(matchesStart[1]);                           
                        var endTmz =  parseInt(matchesEnd[1]);                        

                        var start = new Date(startTmz)                                              
                        var end = new Date(endTmz);                                     
                        var startMinutes = start.getHours() * 60 + start.getMinutes() + offsetToGreenwich * 60 + myOffset * 60;
                        var endMinutes   = end.getHours() * 60 + end.getMinutes() + offsetToGreenwich * 60 + myOffset * 60; 

                        if (endMinutes <= startMinutes) endMinutes += 24 * 60;

                        //var lengthMinutes = (end.getTime() - start.getTime()) / 1000 / 60;
                        
                        var nowMinutes = d.getHours() * 60 + d.getMinutes() + offsetToGreenwich * 60 + myOffset * 60;
                        //var nowMinutes = d.getHours() * 60 + d.getMinutes();
                        // start = new Date(d.getYear(), d.getMonth(), d.getMinutes())
                        //start.setHours(start.setHours()length);
                        

                        if (nowMinutes >= startMinutes && nowMinutes <= endMinutes) {
                            console.log('okkey ', route.RouteID);
                           RTApp.views.selectRouteView.onlineRoutesIds[route.RouteID] = true;
                        }

                        if ((nowMinutes + 24 * 60) >= startMinutes && (nowMinutes + 24 * 60) <= endMinutes) {
                           RTApp.views.selectRouteView.onlineRoutesIds[route.RouteID] = true;
                        }
                        console.log(startMinutes, nowMinutes, endMinutes);
                        console.log(startMinutes, nowMinutes + 24 * 60, endMinutes);
                        
                        // console.log(start, d, end);
                    }                    
                    //alert(n);
                    RTApp.views.selectRouteView.render();
                    console.log('online ', RTApp.views.selectRouteView.onlineRoutesIds);
                  })
                  /*                  
                  for (var rr in routes) {       
                    var route = routes[rr];                                 
                    nextRoute:
                    for (var i in route.RouteStops) {
                      console.log(route.RouteID + " >> check stop");
                      var stop = route.RouteStops[i];
                      for (var j in stop.Estimates) {                                  
                        if (stop.Estimates[j].OnRoute) {
                          RTApp.views.selectRouteView.onlineRoutesIds[route.RouteID] = true;
                          console.log("online out " + route.RouteID);
                          break nextRoute;
                        }                  
                      }
                    }*/
                }                                
              });
              
              $("#selectRouteOnline").addClass("ui-btn-active");
              $("#selectRouteAll").removeClass("ui-btn-active");              
            } else {      
              $("#selectRouteOnline").removeClass("ui-btn-active");
              $("#selectRouteAll").addClass("ui-btn-active");
              // render list!
              RTApp.views.selectRouteView.render();
            }            
          }
        });
      }
    })    
  },  
  render: function(){     
    console.log("SelectRouteView - render");           
    RTApp.setTitle("Select route");     
    // why it calling twice???
    if(RTApp.currentState.client) {
      var html = '';
      if (!this.onlineMode) {
        html += '<li><a href="#map/' + RTApp.currentState.client.get('ClientID') + '">Show all vehicles on route</a></li>';      
      }      
      
      var routes_ = this.model.toJSON();

      // remove all routes that IsVisibleOnMap = false
      var hasSomthingToShow = false;

      // shit code :((((
      for (var i in routes_) {
        var route = routes_[i];        
        if (route.IsVisibleOnMap === undefined)  route.IsVisibleOnMap = true; // if WS is not uptodate
        // delete from routes list if flag visible set to false or if online mode and route is offline
        if (!route.IsVisibleOnMap || (this.onlineMode && this.onlineRoutesIds[route.RouteID] === undefined)) {          
          delete routes_[i];
          continue;
        }
        hasSomthingToShow = true;              
      }
      
      if (this.onlineMode && !hasSomthingToShow) {
        html += '<li class="tac">No Active Routes</li>';      
      } else {
        html += _.template(this.template, {routes: routes_, client: RTApp.currentState.client} );
      }      
      $("#selectRouteList", this.$el).html(html).listview('refresh');
      $.mobile.hidePageLoadingMsg();
    }
    return this;
  }
});

RTApp.views.ArrivalsView = Backbone.View.extend({
  el: "#arrivals",  
  template: "<% _.each(stops, function(stop) { %><li><%= stop.Description %></li><% }); %>",
  initialize: function() {
    console.log("ArrivalsView - initialize");    
    this.model = RTApp.model.estimates;
    _.bindAll(this, 'render')    
              // render after routes  will be loaded    
  },
  onBeforeShow: function() {
    RTApp.fixHeight(this.$el);
    //$(document.body).css({overflow: 'hidden'});
    this.stopEstimatesPolling();
    // this.model.bind('reset', this.render, this);
  },
  onBeforeHide: function() {
    //$(document.body).css({overflow: 'auto'});
    //this.startBusPolling();
    // this.model.unbind('reset', this.render, this);
  },
  load: function(clientId, routeId) {
    $.mobile.showPageLoadingMsg();
    RTApp.model.clients.fetch({
      success: function() {
        RTApp.currentState.client =  RTApp.model.clients.get(clientId);       
        if (!RTApp.currentState.client) return RTApp.goToSelectAgency(); 
        if (routeId) {
          RTApp.model.estimates.fetch(RTApp.currentState.client.get("WebAddress"), routeId, {
            success: function() {
              RTApp.views.arrivalsView.render();
            }
          });
        } else {
          RTApp.views.arrivalsView.selectRouteFirst();
        }
      }
    })
  },  

  selectRouteFirst: function() {
    console.log("ArrivalsView - select selectRouteFirst");
    RTApp.setTitle("Arrivals"); 
    $("ul", this.$el).html("").listview('refresh');
    $.mobile.hidePageLoadingMsg();
    $("#goToSelectRoute").show();
  },
  render: function(){    
    RTApp.setTitle("Arrivals"); 
    console.log("ArrivalsView - render");           
        
    if(RTApp.currentState.client) {      
      var html = '';
      var arrivals = this.model.toJSON();
      if (arrivals.length > 0 && arrivals[0].RouteStops && arrivals[0].RouteStops.length > 0) {
        // pre process an         
        html += _.template($("#estimatesTemplate").html(), {stops: this.model.toJSON()[0].RouteStops, client: RTApp.currentState.client} );
        $("ul", this.$el).html(html).listview('refresh');
      } else {
        $("ul", this.$el).html("<li class='tac'>No times available</li>").listview('refresh');        
      }
      $("#goToSelectRoute").hide();      
    }
     $.mobile.hidePageLoadingMsg();
    return this;
  },
  startEstimatesPolling: function() {
    if (!this.estimatesPollingIntervalID) {     
      var toExec = function() { RTApp.model.estimates.fetch(RTApp.currentState.client.get("WebAddress")); }
      toExec(); // first call
      this.estimatesPollingIntervalID = setInterval(toExec, RTApp.cfg.BUS_REFRESH_TIME);    
    }
  },
  stopEstimatesPolling: function(){
    clearInterval(this.estimatesPollingIntervalID);
    this.estimatesPollingIntervalID = null;   
  },  
  events: {
    'click #goToSelectRoute' : function() {
      window.location.href = "#routes/" + RTApp.currentState.client.get("ClientID"); 
      return false;
    }
  }

});


RTApp.views.RouteSchedule = Backbone.View.extend({
  el: "#routeSchedule",  
  initialize: function() {
    console.log("RouteSchedule - initialize");            
  },   
  load: function(clientId, routeId) {
    $.mobile.showPageLoadingMsg();
    RTApp.model.clients.fetch({
      success: function() {
        RTApp.currentState.client =  RTApp.model.clients.get(clientId);       
        if (!RTApp.currentState.client) return RTApp.goToSelectAgency(); 
        RTApp.model.routes.fetch(RTApp.currentState.client.get("WebAddress"), {
          success: function() {
            RTApp.views.routeSchedule.model = RTApp.model.routes.get(routeId);
            RTApp.views.routeSchedule.render();
          }
        });      
      }
    })
  },
  imageZoom: 100,
  render: function() {     
    var ts = null;
    $('#zoomContainer').bind('touchstart',function(e){
      if (ts == null) ts = e.originalEvent.touches[0] || e.originalEvent.changedTouches[0];      
    });
    $('#zoomContainer').bind('touchmove',function(e){
      e.preventDefault();
      var touch = e.originalEvent.touches[0] || e.originalEvent.changedTouches[0];            
      var dx  = ts.pageX - touch.pageX;
      var dy  = ts.pageY - touch.pageY;
      var ml = parseInt($("#zoomContainer img").css("margin-left").replace("px", ""));
      var mt = parseInt($("#zoomContainer img").css("margin-top").replace("px", ""));

      $("#zoomContainer img").css("marginLeft", ml - dx);
      $("#zoomContainer img").css("marginTop", mt - dy);

      var log = $("<i>").html((ml - dx) + "," + (mt - dy) + "; ");
      //$('#routeSchedule .log').append(log);

      ts = touch;
    });
    $('#zoomContainer').bind('touchend',function(e){
      ts = null;
    });

    // $("#zoomContainer, #zoomContainer img").bind('scrollstart', function(e, ui) {
    //   alert('scrollstart');
    //   console.log('scrollstart', e, ui);
    //   //$("#zoomContainer").
    // })    
    // $("#zoomContainer").bind('scrollstop', function(e) {
    //   alert('scrollend');
    //   console.log('scrollend', e);     
    // })
    console.log("RouteSchedule - render");               
    RTApp.setTitle(this.model.get('Description'));
    $("img", this.$el).attr("src", this.model.get('StopTimesPDFLink')).attr("width", this.imageZoom + "%");
    $.mobile.hidePageLoadingMsg();
    return this;
  },
  events: {
    'click #zoomin': 'zoomin',
    'click #zoomout': 'zoomout'
  },
  zoomin: function() {
    this.imageZoom += 33;
    $("img", this.$el).attr("width", this.imageZoom + "%");
  },
  zoomout: function() {
    this.imageZoom -= 33;
    $("img", this.$el).attr("width", this.imageZoom + "%");
  }
});

RTApp.views.NewsView = Backbone.View.extend({
  el: "#news",  
  template: "<% _.each(twitts, function(twi) { %> <li><%= twi.text %><br/> <i class='twi-date'><%= twi.created_at.substring(0, twi.created_at.length - 11) %></i></li> <% }); %>",
  initialize: function() {
    console.log("NewsView - initialize");    
    this.model = RTApp.model.twitts;
    _.bindAll(this, 'render')        
  }, 
  onBeforeShow: function() {    
    RTApp.fixHeight(this.$el);
    // this.model.bind('reset', this.render, this);          // render after routes  will be loaded    
  },
  onBeforeHide: function() {    
    // this.model.unbind('reset', this.render, this);
  }, 
  load: function(clientId) {
    RTApp.setTitle("Alerts");    
    $.mobile.showPageLoadingMsg();
    RTApp.model.clients.fetch({
      success: function() {
        RTApp.currentState.client =  RTApp.model.clients.get(clientId);   
        if (!RTApp.currentState.client) return RTApp.goToSelectAgency();      
        RTApp.views.newsView.model.fetch(RTApp.currentState.client.get('TwitterTag'), 1, {
          success: function() {                                                
            RTApp.views.newsView.render();
          }
        });        
      }
    })        
  },
  render: function(){             
    console.log("NewsView - render");           
    //RTApp.model.twitts  
    var twi = this.model.toJSON();
    console.log(twi);
    if (twi.length > 0) {
      var html = _.template(this.template, {twitts: twi});      
      $("ul", this.$el).html(html).listview('refresh');
    } else {      
      $("ul", this.$el).html("<li class='tac'>No alerts</li>").listview('refresh');
    }    
    $.mobile.hidePageLoadingMsg();     
    return this;
  },
  loadMore: function () {    
    $.mobile.showPageLoadingMsg();
    //this.model.fetch("fcdk", ++this.lastLoadedPage);
  },
  events: {
    'click .loadMore': 'loadMore'
  }
});

Backbone.jsonpSync = function (method, model, options) {    
    options.timeout = 10000; // required, or the application won't pick up on 404 responses
    options.dataType = "jsonp";    
    options.error = function() {        
      RTApp.flashMessage("Internet connection problem");
    }
    return Backbone.sync(method, model, options);
};

RTApp.flashMessage = function(msg) {
  $.mobile.hidePageLoadingMsg();
  $("#flashMessage").html(msg).fadeIn(500, function() {
    setTimeout(function() {
      $("#flashMessage").fadeOut(500);
    }, 3000);    
  })
}

RTApp.model.CachebleCollection = Backbone.Collection.extend({  
  initialize: function() {    
      if (!this.cacheKey) alert("Cache Key is not defined");
      this.on("reset", function(models, options) {                      
        var key = jQuery.isFunction(this.cacheKey) ? this.cacheKey() : this.cacheKey;
        if (!options.skipCacheWrite) {          
          console.log("write cache to localstorage for key " + key);          
          LocalStorageManager.set(key, this.toJSON());                  
        } else {
          console.log("skip write cache to localstorage for key " + key);
        }
      });
    },
    fetch: function(options) {
      var key = jQuery.isFunction(this.cacheKey) ? this.cacheKey() : this.cacheKey;      
      console.log("look in localstorage by key " + key);
      var cache = LocalStorageManager.get(key);
      if (cache) {      
        console.log("got cahce!");    
        var res = this.reset(cache, {skipCacheWrite: true}); 
        if (options && options.success) options.success();
        return res;
      } 
      console.log("cahce miss");
      options || (options = {});      
      return Backbone.Collection.prototype.fetch.call(this, options);
    }
});

RTApp.model.Clients = RTApp.model.CachebleCollection.extend({
  cacheKey: RTApp.C_LOCALSTORAGE_CACHE_4_GET_CLIENTS,  
  url: RTApp.cfg.urls.GET_CLIENTS_URL,  
  sync: Backbone.jsonpSync,
  parse: function(response) {
    for(var i in response) {
      response[i].id = response[i].ClientID;
    }    
    return response;
  }
});

RTApp.model.Routes = RTApp.model.CachebleCollection.extend({
  webAddress: "",
  cacheKey: function() {
    return RTApp.C_LOCALSTORAGE_CACHE_4_GET_ROUTES + this.webAddress;
  },    
  url: function() {
    

    // if (this.webAddress == "www.uofubus.com") {
    //   return 'http://www.uofubus.com/dev/services/Jsonprelay.svc/GetRoutes';
    // } else {
      return RTApp.cfg.urls.GET_ROUTES_URL.replace("{WebAddress}", this.webAddress);
    // }    
  },
  sync: Backbone.jsonpSync,
  fetch: function(webAddress, options) {
    this.webAddress = webAddress;
    options || (options = {});
    options.beforeSend = function(xjr, settings) {
       settings.url = settings.url.replace("callback=", "method=");
    }
    return RTApp.model.CachebleCollection.prototype.fetch.call(this, options);
  },
  parse: function(response) {
    for(var i in response) {
      response[i].id = response[i].RouteID;
    }    
    return response;
  }
});

RTApp.model.Stops = RTApp.model.CachebleCollection.extend({
  webAddress: "",
  cacheKey: function() {
    return RTApp.C_LOCALSTORAGE_CACHE_4_GET_STOPS + this.webAddress;
  },    
  url: function() {
    return RTApp.cfg.urls.GET_STOPS_URL.replace("{WebAddress}", this.webAddress);
  },
  fetch: function(webAddress, options) {
    this.webAddress = webAddress;
    options || (options = {});
    options.beforeSend = function(xjr, settings) {
       settings.url = settings.url.replace("callback=", "method=");
    }
    return RTApp.model.CachebleCollection.prototype.fetch.call(this, options);
  },
  sync: Backbone.jsonpSync,
  parse: function(response) {
    for(var i in response) {
      
    }    
    return response;
  }
});

//http://www.gowesttransit.com/Services/JSONPRelay.svc/GetMapVehiclePoints
RTApp.model.Buses = Backbone.Collection.extend({
  webAddress: "",  
  url: function() {
    return RTApp.cfg.urls.GET_BUSES_URL.replace("{WebAddress}", this.webAddress);
  },
  fetch: function(webAddress, options) {
    this.webAddress = webAddress;
    options || (options = {});
    options.beforeSend = function(xjr, settings) {
       settings.url = settings.url.replace("callback=", "method=");
    }
    return Backbone.Collection.prototype.fetch.call(this, options);
  },
  parse: function(response) {
    for(var i in response) {
      response[i].id = response[i].VehicleID;
    }    
    return response;
  },
  sync: Backbone.jsonpSync
});

RTApp.model.Estimates = Backbone.Collection.extend({
  webAddress: "",  
  url: function() {
    return RTApp.cfg.urls.GET_ESTIMATES_URL.replace("{WebAddress}", this.webAddress).replace("{RouteID}", this.routeId);
  },
  fetch: function(webAddress, routeId, options) {
    this.webAddress = webAddress;
    this.routeId = routeId;
    options || (options = {});
    options.beforeSend = function(xjr, settings) {
       settings.url = settings.url.replace("callback=", "method=");
    }
    return Backbone.Collection.prototype.fetch.call(this, options);
  },
  sync: Backbone.jsonpSync,
  parse: function(response) {
    if (response[0] && response[0].RouteStops && response[0].RouteStops.length > 0) {
      for (var stopIndex in response[0].RouteStops) {
        var stop = response[0].RouteStops[stopIndex];
        var estimates = stop.Estimates;            
        response[0].RouteStops[stopIndex].Estimates = []
        // remove off road
        for (var i in estimates) {
          var vehicleID = estimates[i].VehicleID              
          if (estimates[i].OnRoute) { // if on route and remove duplicates
            response[0].RouteStops[stopIndex].Estimates[estimates[i].SecondsToStop] = estimates[i];           
          }
        }
      }      
      return response;
    }
  }  
});

RTApp.model.Twitts = Backbone.Collection.extend({
  tag: '', 
  page: 1, 
  url: function() {
    var url = RTApp.cfg.urls.TWITTER_URL.replace("{q}", this.tag).replace("{page}", this.page).replace("{rpp}", RTApp.cfg.TWITTS_PER_PAGE); 
    console.log("twitts url request", url);
    return url;
  },
  fetch: function(tag, page, options) {  
    this.tag = tag;
    this.page = page;
    options || (options = {});    
    return Backbone.Collection.prototype.fetch.call(this, options);
  },
  sync: Backbone.jsonpSync,
  parse: function(response) {  
    return response;
  }
});

/** UTILS **/

var LocalStorageManager = new function() {

  function isAvailable() {
    return (typeof(localStorage) != 'undefined');   
  }

  return { 
    set: function(k,v) {
      if (!isAvailable()) return false;
      try {        
        localStorage.setItem(k, JSON.stringify(v));
      } catch (e) {
        // out of memory
      }
    }, 
    get: function(k) {
      if (!isAvailable()) return false;
      var o = localStorage.getItem(k)
      if (o) return JSON.parse(o);    
      return false;
    },    
    remove: function(k) {
      if (!isAvailable()) return false;
      return localStorage.removeItem(k);
    },
    clear: function() {
      if (!isAvailable()) return false;
      localStorage.clear()
    }    
  }
}

var Canon = new function() {  
  var eventFunction;
  var countdown
  return {
    charge: function(_countdown, _eventFunction) {
      countdown = _countdown;
      eventFunction = _eventFunction;
    },
    tick: function() {
      countdown--;
      if (countdown == 0) eventFunction();
    }

  }
}


/** INIT WS API GATEWAYS **/
RTApp.model.clients = new RTApp.model.Clients();
RTApp.model.routes  = new RTApp.model.Routes();
RTApp.model.stops   = new RTApp.model.Stops();
RTApp.model.buses   = new RTApp.model.Buses();
RTApp.model.twitts  = new RTApp.model.Twitts();
RTApp.model.estimates = new RTApp.model.Estimates();


RTApp.model.fetchClientsSettings = function(webAddress, settingsName, callback) {
  var cacheKey = RTApp.C_LOCALSTORAGE_CACHE_4_GET_CLIENT_SETTINGS + "_" + webAddress + "_" + settingsName;
  var result = LocalStorageManager.get(cacheKey);
  if (!result) {      
    $.ajax({
        url: RTApp.cfg.urls.GET_CLIENT_SETTINGS.replace('{WebAddress}', webAddress), 
        data: {SettingName: settingsName},
        dataType: 'jsonp',
        beforeSend: function(xjr, settings) {
          settings.url = settings.url.replace("callback=", "method=");
        },        
      }).done(function(res) {   
        if (res && res[0]) {          
          LocalStorageManager.set(cacheKey, res[0]);
          if (callback) callback(res[0])
        } else {
          if (callback) callback(null)
        }        
      });
  } else {
    if (callback) callback(result);
  } 
}

RTApp.model.getClientsSettings = function(webAddress, settingsName) {
  var cacheKey = RTApp.C_LOCALSTORAGE_CACHE_4_GET_CLIENT_SETTINGS + "_" + webAddress + "_" + settingsName;
  return LocalStorageManager.get(cacheKey);  
}

RTApp.model.fetchRouteSchedules = function(webAddress, callback) {
  var cacheKey = RTApp.C_LOCALSTORAGE_CACHE_4_GET_ROUTE_SCHEDULES + "_" + webAddress;
  var result = LocalStorageManager.get(cacheKey);
  if (!result) {      
    $.ajax({
        url: RTApp.cfg.urls.GET_ROUTE_SCHEDULES.replace('{WebAddress}', webAddress),         
        dataType: 'jsonp',
        beforeSend: function(xjr, settings) {
          settings.url = settings.url.replace("callback=", "method=");
        },        
      }).done(function(res) {   
        if (res) {          
          LocalStorageManager.set(cacheKey, res);
          if (callback) callback(res)
        } else {
          if (callback) callback(null)
        }        
      });
  } else {
    if (callback) callback(result);
  } 
}

function BusMarker(bus, map) {    
  this.bus_ = bus;
  this.map_ = map;
  this.div_ = null;
  this.directionImage_ = null;
  this.setMap(map);
}

BusMarker.prototype = new google.maps.OverlayView();

BusMarker.prototype.updateBus = function(bus) {
  this.bus_ = bus;
  this.draw();
}

BusMarker.prototype.onAdd = function() {
  var div = document.createElement('DIV');  

  div.className = "busMarker";
  div.setAttribute("rel", this.bus_.get("VehicleID"));
  div.style.backgroundColor = "red";

  var image = document.createElement('IMG');
  image.src = "resources/images/markers/N.png";  
  image.setAttribute("rel", this.bus_.get("VehicleID"));

  div.appendChild(image);
  
  this.div_ = div;
  this.directionImage_ = image;
  var panes = this.getPanes();
  this.getPanes().floatPane.style['zIndex'] = 4000;
  panes.floatPane.appendChild(div);
  
  var me = this;
  google.maps.event.addDomListener(this.div_, 'click', function() {
    google.maps.event.trigger(me, 'click');
  });
}

BusMarker.prototype.draw = function() {
  var overlayProjection = this.getProjection();
  var xycenter = overlayProjection.fromLatLngToDivPixel(new google.maps.LatLng(this.bus_.get('Latitude'), this.bus_.get('Longitude')));
  var div = this.div_;
  
  var image = this.directionImage_;
  div.style.left = (xycenter.x - 15) + 'px';
  div.style.top = (xycenter.y - 15) + 'px';  

  if (this.bus_.get('GroundSpeed') > 4) {
    image.src = "resources/images/markers/N.png";
    image.style['-webkit-transform'] = 'rotate(' + this.bus_.get('Heading') + 'deg)';
  } else {
    image.src = "resources/images/markers/stop.png";
    image.style['-webkit-transform'] = 'rotate(0deg)';
  }  

  var original = '#ff0000'
  var route = RTApp.model.routes.get(this.bus_.get('RouteID'));
  if (route) {        
    original = route.get('MapLineColor');
  } 
    
  var dark = hexDarker(original, 30);  
  div.style['background-image'] = '-webkit-gradient(linear, 0% 36%, 50% 100%, from('+original+'), to(#'+dark+'))';
}

BusMarker.prototype.onRemove = function() {
  this.div_.parentNode.removeChild(this.div_);
  this.div_ = null;
}

BusMarker.prototype.hide = function() {
  if (this.div_) this.div_.style.visibility = "hidden";  
}
BusMarker.prototype.show = function() {  
  if (this.div_) this.div_.style.visibility = "visible";  
}

BusMarker.prototype.toggle = function() {
  if (this.div_) {
    if (this.div_.style.visibility == "hidden") {
      this.show();
    } else {
      this.hide();
    }
  }
}

BusMarker.prototype.toggleDOM = function() {
  if (this.getMap()) {
    this.setMap(null);
  } else {
    this.setMap(this.map_);
  }
}


function RouteStopMarker(stop, map, showlabel) {    
  this.stop_ = stop;
  this.map_ = map;
  this.div_ = null;
  this.showlabel_ = showlabel;
  this.setMap(map);
}

RouteStopMarker.prototype = new google.maps.OverlayView();


RouteStopMarker.prototype.onAdd = function() {
  var div = document.createElement('DIV');
  div.className = "stopMarker"
  div.setAttribute("rel", this.stop_.RouteStopID);
  var image = document.createElement('IMG');
  image.src = "resources/images/markers/stop_marker.png";  
  image.setAttribute("rel", this.stop_.RouteStopID);
  div.appendChild(image);

  var label = document.createElement('LABEL'); 
  label.innerHTML = this.stop_.Description;
  label.className = "stopLabel";
  label.setAttribute("rel", this.stop_.RouteStopID);
  if (this.showlabel_) div.appendChild(label);
  
  this.div_ = div;
  this.label_ = label;
  var panes = this.getPanes();
  this.getPanes().floatPane.style['zIndex'] = 3000;
  panes.floatPane.appendChild(div);

  var me = this;
  google.maps.event.addDomListener(this.div_, 'click', function() {
    google.maps.event.trigger(me, 'click');    
  });
  // google.maps.event.addDomListener(this.label_, 'click', function() {
  //   google.maps.event.trigger(me, 'click');    
  // });
  // google.maps.event.addDomListener(image, 'click', function() {
  //   google.maps.event.trigger(me, 'click');    
  // });
}

RouteStopMarker.prototype.draw = function() {
  var overlayProjection = this.getProjection();    
  var xycenter = overlayProjection.fromLatLngToDivPixel(new google.maps.LatLng(this.stop_.Latitude, this.stop_.Longitude));
  var div = this.div_;
  //this.label_.style.visibility = (this.map_.getZoom() > 14) ? "visible" : "hidden";
  div.style.left = (xycenter.x - 8) + 'px';
  div.style.top = (xycenter.y - 8) + 'px';  
}

RouteStopMarker.prototype.onRemove = function() {
  this.div_.parentNode.removeChild(this.div_);
  this.div_ = null;
}
RouteStopMarker.prototype.hide = function() {
  if (this.div_) this.div_.style.visibility = "hidden";  
}
RouteStopMarker.prototype.show = function() {  
  if (this.div_) this.div_.style.visibility = "visible";  
}

RouteStopMarker.prototype.toggle = function() {
  if (this.div_) {
    if (this.div_.style.visibility == "hidden") {
      this.show();
    } else {
      this.hide();
    }
  }
}

RouteStopMarker.prototype.toggleDOM = function() {
  if (this.getMap()) {
    this.setMap(null);
  } else {
    this.setMap(this.map_); 
  }
}


Dialog = function(id) {
  var ctx;
  var $d = $(id);
  $(".close", $d).click(function() {
    ctx.close();
    return false;
  })
  return ctx = {
    show: function(title, content) {
      if (title) $("h1", $d).html(title);
      $(".text-content", $d).html("");
      if (content && (typeof content == "string") ) $(".text-content", $d).html(content);
      if (content && (typeof content == "object") ) $(".text-content", $d).append(content);
      $d.css({top: document.body.scrollTop + 30})
      $d.show();
    },
    close: function() {
      $d.hide();
    },
    setPrimaryAction: function(url, name, onclick) {
      $(".primary-action", $d).attr("href", url).show();
      $(".ui-btn-text", $(".primary-action", $d)).html(name);
      if (onclick) $(".primary-action", $d).click(onclick)
    },
    setSecondaryAction: function(url, name, onclick) {
      $(".secondary-action", $d).attr("href", url).show();
      $(".ui-btn-text", $(".secondary-action", $d)).html(name);
      if (onclick) $(".secondary-action", $d).click(onclick)
    },
    hideActions: function() {
      $(".secondary-action", $d).hide();
      $(".primary-action", $d).hide();
    },
    showBus: function(bus) {
      this.hideActions();
      var route = RTApp.model.routes.get(bus.get("RouteID"));
      $("h1", $d).html( route.get("Description") + " Route - #" + bus.get('Name'));
      $(".text-content", $d).html('loading...');
      RTApp.model.estimates.fetch(RTApp.currentState.client.get("WebAddress"), bus.get("RouteID"), {
        success: function(res) {
          var routes = res.toJSON();
          var nextStopInSeconds = 1000000; // max
          var nextStop = null; 
          var prevNextStopInSeconds = 1000000; // max
          var prevNextStop = null; 
          if (routes[0] && routes[0].RouteStops && routes[0].RouteStops.length > 0) {
            for (var i in routes[0].RouteStops) {
              var stop = routes[0].RouteStops[i];
              for (var j in stop.Estimates) {
                var estimate = stop.Estimates[j];
                if (estimate.VehicleID == bus.get('VehicleID') && estimate.OnRoute && estimate.SecondsToStop < nextStopInSeconds) {
                  prevNextStopInSeconds = nextStopInSeconds;
                  prevNextStop = nextStop;
                  nextStop = stop;
                  nextStopInSeconds = estimate.SecondsToStop;
                }
              }
            }
          }
          // ok we got a stom
          if (nextStop) {
            var min = Math.round(nextStopInSeconds / 60);
            var msg = "";
            if (min != 0) {
              msg =  nextStop.Description + ' ' + min + " min";
            } else {
              msg = 'Arriving at ' + nextStop.Description;
            }        

            if (prevNextStop) {
              msg += "<br/>";
              var min = Math.round(prevNextStopInSeconds / 60);              
              if (min != 0) {
                msg += prevNextStop.Description + ' ' + min + " min";
              } else {
                msg += 'Arriving at ' + prevNextStop.Description;
              }               
            }
            $(".text-content", $d).html(msg);
            // show route only when bus in on a route
            if (!RTApp.currentState.route || RTApp.currentState.route.get('RouteID') != bus.get('RouteID')) {
              RTApp.dialogs.common.setSecondaryAction("#map/" + RTApp.currentState.client.get('id') + "/" + bus.get('RouteID'), "Show route on map");
            }            
          
          } else {
            $(".text-content", $d).html('No times available');
          }
        }
      })
      var routeUrl = RTApp.cfg.urls.GET_ROUTES_URL.replace("{WebAddress}", RTApp.currentState.client.get('WebAddress'));
      $.ajax({
        url: routeUrl, 
        data: {RouteID: bus.get('RouteID')},
        dataType: 'jsonp',
        beforeSend: function(xjr, settings) {
          settings.url = settings.url.replace("callback=", "method=");
        }
      }).done(function(res) {
        console.log(res);      
        if (res && res[0] && res[0].StopTimesPDFLink) {                  
          RTApp.dialogs.common.setPrimaryAction("img.html", "View Schedule", function() {

             LocalStorageManager.set("url_of_image", {url: res[0].StopTimesPDFLink});
             window.location.href = "img.html";
           });

          // RTApp.dialogs.common.setPrimaryAction("#routeSchedule/" + RTApp.currentState.client.get('ClientID') + "/"+ res[0].RouteID, "View Schedule");          
        }
      });      
      this.show();
    }, 
    showStop: function(stop) {
      this.hideActions();
      $("h1", $d).html(stop.Description);
      $(".text-content", $d).html('loading...');
      RTApp.model.estimates.fetch(RTApp.currentState.client.get("WebAddress"), stop.RouteID, {
        success: function(res) {
          var routes = res.toJSON();        
          var nextArrivals = ""; 
          if (routes[0] && routes[0].RouteStops && routes[0].RouteStops.length > 0) {
            for (var i in routes[0].RouteStops) {
              var _stop = routes[0].RouteStops[i];
              if (stop.RouteStopID == _stop.RouteStopID) {
                for (var j in _stop.Estimates) {
                  var estimate = _stop.Estimates[j];                
                  if (estimate.OnRoute) {
                    var min = Math.round(estimate.SecondsToStop / 60);
                    if (min == 0) {
                      nextArrivals += "now, ";
                    } else {
                      nextArrivals += min + " min, ";
                    }
                  }                  
                }
                if (nextArrivals != "") nextArrivals = nextArrivals.substring(0, nextArrivals.length - 2);
              }
            }
          }
          // ok we got a stom          
            if (nextArrivals != "") {
              if (nextArrivals == 'now') {
                $(".text-content", $d).html('Now Arriving');
              } else {
                $(".text-content", $d).html('Next arrival ' + nextArrivals);
              }              
            } else {
              $(".text-content", $d).html('No buses on route');
            }                      
        }
      })
      this.show();
    }
  }
}

function hexDarker($hex, $factor) {
  if ($hex[0] == '#') $hex = $hex.substring(1);
  var $new_hex = '';
  
  function d2h(d) {return d.toString(16);}
  function h2d(h) {return parseInt(h,16);}
  var $base = [];
  $base.push(h2d($hex.substring(0,2)));
  $base.push(h2d($hex.substring(2,4)));
  $base.push(h2d($hex.substring(4,6)));
    
  for (var i = 0; i < $base.length; i++) {
    var $v = $base[i];    
    var $amount = $v / 100;
      $amount = Math.round($amount * $factor);  
      $new_decimal = $v - $amount;
      $new_hex_component = d2h($new_decimal);
      if($new_hex_component.length < 2)  $new_hex_component = "0" + $new_hex_component;
      $new_hex += $new_hex_component;
  }         
  return $new_hex;        
}




RTApp.mapStyles = [
  {
    featureType: "poi",
    stylers: [
      { visibility: "off" }
    ]
  },{
    featureType: "administrative.country",
    stylers: [
      { visibility: "off" }
    ]
  },{
    featureType: "administrative.province",
    stylers: [
      { visibility: "off" }
    ]
  },{
    featureType: "administrative.locality",
    stylers: [
      { visibility: "off" }
    ]
  },{
    featureType: "administrative.neighborhood",
    stylers: [
      { visibility: "off" }
    ]
  },{
    featureType: "water",
    stylers: [
      { visibility: "simplified" }
    ]
  },{
    featureType: "landscape",
    stylers: [
      { visibility: "off" }
    ]
  },{
    featureType: "poi",
    stylers: [
      { visibility: "off" }
    ]
  },{
    featureType: "transit",
    stylers: [
      { visibility: "off" }
    ]
  },{
    featureType: "water",
    stylers: [
      { visibility: "simplified" }
    ]
  },{
  }
]
