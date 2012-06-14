using System;
using System.Collections.Generic;
using System.Text;
using Ride.Client.PocketPC.RideSyncClient;
using System.Xml;
using Ride.Controls.Compact.Waiting;

namespace Ride.Client.PocketPC.Data
{
    class SyncController
    {
        #region Events
            public event EventHandler SyncedBusinessTypes;
            public event EventHandler SyncedVehiclesAndEmployees;
            public event EventHandler SyncedRouteDefinitions;
            public event EventHandler SyncedDVIRPrompts;
        #endregion

        #region Private Variables
            private string _url;
            private RideCredential _rideCredential;
        #endregion

        #region Constructor
            public SyncController(XmlNode parent)
            {
                _rideCredential = new RideCredential();
                Ride.Business.Objects.Authentication.RideCredentialSetting credentialSetting = null;
                foreach (XmlNode node in parent.ChildNodes)
                {
                    if (string.Compare(node.Name, "Sync_URL", true) == 0)
                        _url = node.InnerText;
                    else if (string.Compare(node.Name, "Authentication", true) == 0)
                        credentialSetting = new Ride.Business.Objects.Authentication.RideCredentialSetting(node);
                }
                if (credentialSetting != null)
                {
                    _rideCredential.CompanyID = credentialSetting.CompanyID;
                    _rideCredential.CompanyIDSpecified = true;
                    _rideCredential.Token = credentialSetting.Token;
                    _rideCredential.EmployeeID = credentialSetting.EmplpoyeeID;
                    _rideCredential.EmployeeIDSpecified = true;
                }
            }
        #endregion

        #region Public Properties
            public string URL
            {
                get { return _url; }
                set { _url = value; }
            }
        #endregion

        #region Private Methods
            private RideSyncClient.RideWCFServiceImplementation GetSyncClient()
            {
                RideSyncClient.RideWCFServiceImplementation client = new Ride.Client.PocketPC.RideSyncClient.RideWCFServiceImplementation();
                if (!string.IsNullOrEmpty(_url))
                    client.Url = _url;
                return client;
            }
            #region Raise Events
                private void RaiseSyncedBusinessTypes()
                {
                    if (SyncedBusinessTypes != null)
                    {
                        SyncedBusinessTypes(null, new EventArgs());
                    }
                }
                private void RaiseSyncedVehiclesAndEmployees()
                {
                    if (SyncedVehiclesAndEmployees != null)
                    {
                        SyncedVehiclesAndEmployees(null, new EventArgs());
                    }
                }
                private void RaiseSyncedRouteDefinitions()
                {
                    if (SyncedRouteDefinitions != null)
                    {
                        SyncedRouteDefinitions(null, new EventArgs());
                    }
                }
                private void RaiseSyncedDVIRPrompts()
                {
                    if (SyncedDVIRPrompts != null)
                    {
                        SyncedDVIRPrompts(null, new EventArgs());
                    }
                }
            #endregion
        #endregion

        #region Public Methods
            public void SyncVehiclesAndEmployees()
            {
                EmployeeVehicleResponse response = null;
                try
                {
                    using (RideWCFServiceImplementation client = GetSyncClient())
                    {
                        VehicleSyncRequest request = new VehicleSyncRequest();
                        request.RideCredential = _rideCredential;
                        request.HistoricalDays = 0;
                        request.HistoricalDaysSpecified = true;
                        request.Request = new RequestArgument();
                        request.Request.RequestType = RequestArgumentRequestTypes.FullSync;
                        request.Request.RequestTypeSpecified = true;
                        request.Request.DateSpecified = false;
                        response = client.GetHandheldEmployeesAndVehicles(request);
                    }

                }
                finally
                {
                    if (response != null && response.Success)
                    {
                        #region Employees
                            Data.DataHolder.Ref.ClearEmployees();
                            foreach (Employee employee in response.Employees)
                            {
                                Data.DataHolder.Ref.SaveEmployee(Converter.Employee(employee));
                            }
                        #endregion

                        #region Vehicles
                            Data.DataHolder.Ref.ClearVehicles();
                            foreach (Vehicle employee in response.Vehicles)
                            {
                                Data.DataHolder.Ref.SaveVehicle(Converter.Vehicle(employee));
                            }
                        #endregion
                        RaiseSyncedVehiclesAndEmployees();
                    }
                }
            }
            public void SyncBusinessTypes()
            {
                BusinessTypeResponse response = null;
                try
                {
                    using (RideWCFServiceImplementation client = GetSyncClient())
                    {
                        GeneralSyncRequest request = new GeneralSyncRequest();
                        request.RideCredential = _rideCredential;
                        request.Request = new RequestArgument();
                        request.Request.RequestType = RequestArgumentRequestTypes.FullSync;
                        request.Request.RequestTypeSpecified = true;
                        request.Request.DateSpecified = false;
                        response = client.GetHandheldBusinessTypes(request);
                    }

                }
                catch (System.Web.Services.Protocols.SoapException ex)
                {
                    Console.Write(ex.Message);
                }
                catch (Exception ex)
                {
                    Console.Write(ex.Message);
                }
                finally
                {
                    if (response != null && response.Success)
                    {
                        #region Employee Types
                            Data.DataHolder.Ref.ClearEmployeeTypes();
                            foreach (EmployeeType employeeType in response.EmployeeTypes)
                            {
                                Data.DataHolder.Ref.SaveEmployeeType(Converter.EmployeeType(employeeType));
                            }
                        #endregion

                        #region Client Types
                            Data.DataHolder.Ref.ClearClientTypes();
                            foreach (ClientType clientType in response.ClientTypes)
                            {
                                Data.DataHolder.Ref.SaveClientType(Converter.ClientType(clientType));
                            }
                        #endregion

                        #region Address Types
                            Data.DataHolder.Ref.ClearAddressTypes();
                            foreach (AddressType addressType in response.AddressTypes)
                            {
                                Data.DataHolder.Ref.SaveAddressType(Converter.AddressType(addressType));
                            }
                        #endregion

                        #region Vehicle Types
                            Data.DataHolder.Ref.ClearVehicleTypes();
                            foreach (VehicleType vehicleType in response.VehicleTypes)
                            {
                                Data.DataHolder.Ref.SaveVehicleType(Converter.VehicleType(vehicleType));
                            }
                        #endregion

                        #region No Show Types
                            Data.DataHolder.Ref.ClearNoShowTypes();
                            foreach (NoShowType noShowType in response.NoShowTypes)
                            {
                                Data.DataHolder.Ref.SaveNoShowType(Converter.NoShowType(noShowType));
                            }
                        #endregion
                        RaiseSyncedBusinessTypes();
                    }
                }
            }
            public void SyncRoutes()
            {
                RouteDefinitionResponse response = null;
                try
                {                    
                    using (RideWCFServiceImplementation client = GetSyncClient())
                    {
                        RouteSyncRequest request = new RouteSyncRequest();
                        request.RideCredential = _rideCredential;
                        request.Request = new RequestArgument();
                        request.Request.RequestType = RequestArgumentRequestTypes.FullSync;
                        request.Request.RequestTypeSpecified = true;
                        request.Request.DateSpecified = false;
                        request.SyncComments = false;
                        request.SyncCommentsSpecified = true;
                        request.SyncLandmarks = false;
                        request.SyncLandmarksSpecified = true;
                        request.SyncMapPoints = false;
                        request.SyncMapPointsSpecified = true;
                        response = client.GetHandheldRoutes(request);
                    }
                }
                finally
                {
                    if (response != null && response.Success)
                    {
                        #region Routes
                            Data.DataHolder.Ref.ClearRoutes();
                            foreach (Route syncRoute in response.Routes)
                            {
                                Ride.Business.Objects.Routes.Route route = Converter.Route(syncRoute);
                                route.Save();
                                Data.DataHolder.Ref.SaveRoute(route);
                                foreach (Ride.Business.Objects.Routes.Stops.RouteStop stop in route.Stops.Active)
                                {
                                    Data.DataHolder.Ref.SaveRouteStop(route, stop);
                                    foreach(Ride.Business.Objects.Routes.Stops.RouteStopEvent routeStopEvent in stop.StopEvents.Active)
                                    {
                                        Data.DataHolder.Ref.SaveRouteStopEvent(stop, routeStopEvent);
                                        foreach (Ride.Business.Objects.Routes.Stops.RouteStopEventAudioFile routeStopEventAudioFile in routeStopEvent.AudioFiles)
                                        {
                                            Data.DataHolder.Ref.SaveAudioFile(routeStopEventAudioFile.AudioFile);
                                            Data.DataHolder.Ref.SaveRouteStopEventAudioFile(routeStopEvent, routeStopEventAudioFile);
                                        }
                                    }
                                }
                            }
                        #endregion

                        #region Tracking Levels
                            Data.DataHolder.Ref.ClearTrackingLevels();
                            foreach (TrackingLevel syncTrackingLevel in response.TrackingLevels)
                            {
                                Ride.Business.Objects.Routes.Tracking.TrackingLevel trackingLevel = Converter.TrackingLevel(syncTrackingLevel);
                                Data.DataHolder.Ref.SaveTrackingLevel(trackingLevel);
                                foreach (Ride.Business.Objects.Routes.Tracking.RouteTrackingCriteria routeTrackingCriteria in trackingLevel.Criteria.Active)
                                {
                                    Data.DataHolder.Ref.SaveRouteTrackingCriteria(trackingLevel, routeTrackingCriteria);
                                }
                            }
                        #endregion
                        RaiseSyncedRouteDefinitions();
                    }
                }

            }
            public void SyncDVIRPrompts()
            {
                DVIRPromptsResponse response = null;
                try
                {
                    using (RideWCFServiceImplementation client = GetSyncClient())
                    {
                        GeneralSyncRequest request = new GeneralSyncRequest();
                        request.RideCredential = _rideCredential;
                        request.Request = new RequestArgument();
                        request.Request.RequestType = RequestArgumentRequestTypes.FullSync;
                        request.Request.RequestTypeSpecified = true;
                        request.Request.DateSpecified = false;
                        response = client.GetHandheldDVIRPrompts(request);
                    }
                }
                catch (System.Web.Services.Protocols.SoapException ex)
                {
                    Console.Write(ex.Message);
                }
                catch (Exception ex)
                {
                    Console.Write(ex.Message);
                }
                finally
                {
                    if (response != null && response.Success)
                    {
                        #region DVIR Prompts
                            Data.DataHolder.Ref.ClearDVIRPrompts();
                            foreach (DVIRPrompt prompt in response.DVIRPrompts)
                            {
                                Data.DataHolder.Ref.SaveDVIRPrompt(Converter.DVIRPrompt(prompt));
                            }
                        #endregion
                    }
                    RaiseSyncedDVIRPrompts();
                }
            }
            public void SyncStaticObjects(IProgress progressUpdate, bool fullSync)
            {
                HandheldStaticSyncResponse response = null;
                HandheldStaticSyncRequest request = new HandheldStaticSyncRequest();
                DateTime syncDate = DateTime.MinValue;
                request.Request = new RequestArgument();
                request.RideCredential = _rideCredential;
                request.HistoricalDays = 0;
                request.HistoricalDaysSpecified = true;
                if (!fullSync)
                    syncDate = Data.DataHolder.Ref.GetSyncDate(DataHolder.SyncTypes.StaticObjects);
                if (syncDate==DateTime.MinValue)
                {
                    request.Request.RequestType = RequestArgumentRequestTypes.FullSync;
                    request.Request.RequestTypeSpecified = true;
                    request.Request.DateSpecified = false;
                }
                else
                {
                    request.Request.RequestType = RequestArgumentRequestTypes.SyncFromDate;
                    request.Request.RequestTypeSpecified = true;
                    request.Request.Date=syncDate;
                    request.Request.DateSpecified = true;
                }
                try
                {
                    using (RideWCFServiceImplementation client = GetSyncClient())
                    {
                        response = client.GetHandheldStaticSync(request);
                    }

                }
                catch (System.Web.Services.Protocols.SoapException ex)
                {
                    Console.Write(ex.Message);
                }
                catch (Exception ex)
                {
                    Console.Write(ex.Message);
                }
                finally
                {
                    if (response != null && response.Success)
                    {
                        #region Save
                            #region Employee Types
                                progressUpdate.IncrementProgress();
                                #region Save
                                    if (response.EmployeeTypes.Length > 0)
                                    {
                                        progressUpdate.SetStatus("Updating Employee Types");
                                        if (request.Request.RequestType == RequestArgumentRequestTypes.FullSync)
                                            Data.DataHolder.Ref.ClearEmployeeTypes();
                                        foreach (EmployeeType employeeType in response.EmployeeTypes)
                                        {
                                            Data.DataHolder.Ref.SaveEmployeeType(Converter.EmployeeType(employeeType));
                                        }
                                    }
                                #endregion
                                #region Delete
                                    if (response.DeletedEmployeeTypes != null && response.DeletedEmployeeTypes.Length > 0)
                                    {
                                        progressUpdate.SetStatus("Deleting Employee Types");
                                        foreach (int employeeType in response.DeletedEmployeeTypes)
                                        {
                                            Data.DataHolder.Ref.DeleteEmployeeType(employeeType);
                                        }
                                    }
                                #endregion
                            #endregion

                            #region Client Types
                                progressUpdate.IncrementProgress();
                                #region Save
                                    if (response.ClientTypes.Length > 0)
                                    {
                                        progressUpdate.SetStatus("Updating Client Types");
                                        if (request.Request.RequestType == RequestArgumentRequestTypes.FullSync)
                                            Data.DataHolder.Ref.ClearClientTypes();
                                        foreach (ClientType clientType in response.ClientTypes)
                                        {
                                            Data.DataHolder.Ref.SaveClientType(Converter.ClientType(clientType));
                                        }
                                    }
                                #endregion
                                #region Delete
                                    if (response.DeletedClientTypes != null && response.DeletedClientTypes.Length > 0)
                                    {
                                        progressUpdate.SetStatus("Deleting Client Types");
                                        foreach (int clientType in response.DeletedClientTypes)
                                        {
                                            Data.DataHolder.Ref.DeleteClientType(clientType);
                                        }
                                    }
                                #endregion
                            #endregion

                            #region Address Types
                                progressUpdate.IncrementProgress();
                                #region Save
                                    if (response.AddressTypes.Length > 0)
                                    {
                                        progressUpdate.SetStatus("Updating Address Types");
                                        if (request.Request.RequestType == RequestArgumentRequestTypes.FullSync)
                                            Data.DataHolder.Ref.ClearAddressTypes();
                                        foreach (AddressType addressType in response.AddressTypes)
                                        {
                                            Data.DataHolder.Ref.SaveAddressType(Converter.AddressType(addressType));
                                        }
                                    }
                                #endregion
                                #region Delete
                                    if (response.DeletedAddressTypes != null && response.DeletedAddressTypes.Length > 0)
                                    {
                                        progressUpdate.SetStatus("Deleting Address Types");
                                        foreach (int addressType in response.DeletedAddressTypes)
                                        {
                                            Data.DataHolder.Ref.DeleteAddressType(addressType);
                                        }
                                    }
                                #endregion
                            #endregion

                            #region Vehicle Types
                                progressUpdate.IncrementProgress();
                                #region Save
                                    if (response.VehicleTypes.Length > 0)
                                    {
                                        progressUpdate.SetStatus("Updating Vehicle Types");
                                        if (request.Request.RequestType == RequestArgumentRequestTypes.FullSync)
                                            Data.DataHolder.Ref.ClearVehicleTypes();
                                        foreach (VehicleType vehicleType in response.VehicleTypes)
                                        {
                                            Data.DataHolder.Ref.SaveVehicleType(Converter.VehicleType(vehicleType));
                                        }
                                    }
                                #endregion
                                #region Delete
                                    if (response.DeletedVehicleTypes != null && response.DeletedVehicleTypes.Length > 0)
                                    {
                                        progressUpdate.SetStatus("Deleting Vehicle Types");
                                        foreach (int vehicleType in response.DeletedVehicleTypes)
                                        {
                                            Data.DataHolder.Ref.DeleteVehicleType(vehicleType);
                                        }
                                    }
                                #endregion
                            #endregion

                            #region No Show Types
                                progressUpdate.IncrementProgress();
                                #region Save
                                    if (response.NoShowTypes.Length > 0)
                                    {
                                        progressUpdate.SetStatus("Updating No Show Types");
                                        if (request.Request.RequestType == RequestArgumentRequestTypes.FullSync)
                                            Data.DataHolder.Ref.ClearNoShowTypes();
                                        foreach (NoShowType noShowType in response.NoShowTypes)
                                        {
                                            Data.DataHolder.Ref.SaveNoShowType(Converter.NoShowType(noShowType));
                                        }
                                    }
                                #endregion
                                #region Delete
                                    if (response.DeletedNoShowTypes != null && response.DeletedNoShowTypes.Length > 0)
                                    {
                                        progressUpdate.SetStatus("Deleting No Show Types");
                                        foreach (int noShowType in response.DeletedNoShowTypes)
                                        {
                                            Data.DataHolder.Ref.DeleteNoShowType(noShowType);
                                        }
                                    }
                                #endregion
                            #endregion
                            
                            #region Employees
                                progressUpdate.IncrementProgress();
                                #region Save
                                    if (response.Employees.Length > 0)
                                    {
                                        progressUpdate.SetStatus("Updating Employees");
                                        if (request.Request.RequestType == RequestArgumentRequestTypes.FullSync)
                                            Data.DataHolder.Ref.ClearEmployees();
                                        foreach (Employee employee in response.Employees)
                                        {
                                            Data.DataHolder.Ref.SaveEmployee(Converter.Employee(employee));
                                        }
                                    }
                                #endregion
                                #region Delete
                                    if (response.DeletedEmployees != null && response.DeletedEmployees.Length > 0)
                                    {
                                        progressUpdate.SetStatus("Deleting Employees");
                                        foreach (int employeeID in response.DeletedEmployees)
                                        {
                                            Data.DataHolder.Ref.DeleteEmployee(employeeID);
                                        }
                                    }
                                #endregion
                            #endregion

                            #region Vehicles
                                progressUpdate.IncrementProgress();
                                #region Save
                                    if (response.Vehicles.Length > 0)
                                    {
                                        progressUpdate.SetStatus("Updating Vehicles");
                                        if (request.Request.RequestType == RequestArgumentRequestTypes.FullSync)
                                            Data.DataHolder.Ref.ClearVehicles();
                                        foreach (Vehicle employee in response.Vehicles)
                                        {
                                            Data.DataHolder.Ref.SaveVehicle(Converter.Vehicle(employee));
                                        }
                                    }
                                #endregion
                                #region Delete
                                    if (response.DeletedVehicles != null && response.DeletedVehicles.Length > 0)
                                    {
                                        progressUpdate.SetStatus("Deleting Vehicles");
                                        foreach (int vehicleID in response.DeletedVehicles)
                                        {
                                            Data.DataHolder.Ref.DeleteVehicle(vehicleID);
                                        }
                                    }
                                #endregion
                            #endregion

                            #region Routes
                                progressUpdate.IncrementProgress();
                                #region Save
                                    if (response.Routes.Length > 0)
                                    {
                                        progressUpdate.SetStatus("Updating Routes");
                                        if (request.Request.RequestType == RequestArgumentRequestTypes.FullSync)
                                            Data.DataHolder.Ref.ClearRoutes();
                                        foreach (Route syncRoute in response.Routes)
                                        {
                                            Ride.Business.Objects.Routes.Route route = Converter.Route(syncRoute);
                                            route.Save();
                                            Data.DataHolder.Ref.SaveRoute(route);
                                            foreach (Ride.Business.Objects.Routes.Stops.RouteStop stop in route.Stops.Active)
                                            {
                                                Data.DataHolder.Ref.SaveRouteStop(route, stop);
                                                foreach (Ride.Business.Objects.Routes.Stops.RouteStopEvent routeStopEvent in stop.StopEvents.Active)
                                                {
                                                    Data.DataHolder.Ref.SaveRouteStopEvent(stop, routeStopEvent);
                                                    foreach (Ride.Business.Objects.Routes.Stops.RouteStopEventAudioFile routeStopEventAudioFile in routeStopEvent.AudioFiles)
                                                    {
                                                        Data.DataHolder.Ref.SaveAudioFile(routeStopEventAudioFile.AudioFile);
                                                        Data.DataHolder.Ref.SaveRouteStopEventAudioFile(routeStopEvent, routeStopEventAudioFile);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                #endregion
                                #region Delete
                                    if (response.DeletedRoutes != null && response.DeletedRoutes.Length > 0)
                                    {
                                        progressUpdate.SetStatus("Deleting Routes");
                                        foreach (int routeID in response.DeletedRoutes)
                                        {
                                            Data.DataHolder.Ref.DeleteRoute(routeID);
                                        }
                                    }
                                    if (response.DeletedRouteStops != null && response.DeletedRouteStops.Length > 0)
                                    {
                                        progressUpdate.SetStatus("Deleting Route Stops");
                                        foreach (int routeStopID in response.DeletedRouteStops)
                                        {
                                            Data.DataHolder.Ref.DeleteRouteStop(routeStopID);
                                        }
                                    }
                                #endregion
                            #endregion

                            #region Tracking Levels
                                progressUpdate.IncrementProgress();
                                #region Save
                                    if (response.TrackingLevels.Length > 0)
                                    {
                                        progressUpdate.SetStatus("Updating Tracking Levels");
                                        if (request.Request.RequestType == RequestArgumentRequestTypes.FullSync)
                                            Data.DataHolder.Ref.ClearTrackingLevels();
                                        foreach (TrackingLevel syncTrackingLevel in response.TrackingLevels)
                                        {
                                            Ride.Business.Objects.Routes.Tracking.TrackingLevel trackingLevel = Converter.TrackingLevel(syncTrackingLevel);
                                            Data.DataHolder.Ref.SaveTrackingLevel(trackingLevel);
                                            foreach (Ride.Business.Objects.Routes.Tracking.RouteTrackingCriteria routeTrackingCriteria in trackingLevel.Criteria.Active)
                                            {
                                                Data.DataHolder.Ref.SaveRouteTrackingCriteria(trackingLevel, routeTrackingCriteria);
                                            }
                                        }
                                    }
                                #endregion
                                #region Delete
                                    if (response.DeletedTrackingLevels != null && response.DeletedTrackingLevels.Length > 0)
                                    {
                                        progressUpdate.SetStatus("Deleting TrackingLevels");
                                        foreach (int trackingLevelID in response.DeletedTrackingLevels)
                                        {
                                            Data.DataHolder.Ref.DeleteTrackingLevel(trackingLevelID);
                                        }
                                    }
                                    if (response.DeletedRouteTrackingCriteria != null && response.DeletedRouteTrackingCriteria.Length > 0)
                                    {
                                        progressUpdate.SetStatus("Deleting Route Tracking Criterias");
                                        foreach (int routeTrackingCriteriaID in response.DeletedRouteTrackingCriteria)
                                        {
                                            Data.DataHolder.Ref.DeleteRouteTrackingCriteria(routeTrackingCriteriaID);
                                        }
                                    }

                                #endregion
                            #endregion

                            #region DVIR Prompts
                                progressUpdate.IncrementProgress();
                                #region Save
                                    if (response.DVIRPrompts.Length > 0)
                                    {
                                        progressUpdate.SetStatus("Updating DVIR Prompts");
                                        if (request.Request.RequestType == RequestArgumentRequestTypes.FullSync)
                                            Data.DataHolder.Ref.ClearDVIRPrompts();
                                        foreach (DVIRPrompt prompt in response.DVIRPrompts)
                                        {
                                            Data.DataHolder.Ref.SaveDVIRPrompt(Converter.DVIRPrompt(prompt));
                                        }
                                    }
                                #endregion
                                #region Delete
                                    if (response.DeletedDVIRPrompts != null && response.DeletedDVIRPrompts.Length > 0)
                                    {
                                        progressUpdate.SetStatus("Deleting DVIR Prompts");
                                        foreach (int dvirPromptID in response.DeletedDVIRPrompts)
                                        {
                                            Data.DataHolder.Ref.DeleteDVIRPrompt(dvirPromptID);
                                        }
                                    }
                                #endregion
                            #endregion
                        #endregion
                        #region Save Last Sync Date
                            if (response.ServerSyncTimeSpecified)
                                Data.DataHolder.Ref.SaveSyncDate(DataHolder.SyncTypes.StaticObjects,response.ServerSyncTime);
                        #endregion
                        RaiseSyncedBusinessTypes();
                        RaiseSyncedVehiclesAndEmployees();
                        RaiseSyncedRouteDefinitions();
                        RaiseSyncedDVIRPrompts();
                    }
                }
            }
            public void SyncRouteInstances(List<RouteInstance> routeInstances)
            {
                if (routeInstances.Count > 0)
                {
                    //Loop while we have records to add
                    while (routeInstances.Count > 0)
                    {
                        #region Send through 150 at a time
                            List<RouteInstance> records = new List<RouteInstance>();
                            for (int x = 0; x < 150; x++)
                            {
                                if (routeInstances.Count > 0)
                                {
                                    records.Add(routeInstances[0]);
                                    routeInstances.Remove(routeInstances[0]);
                                }
                                else
                                    break;
                            }
                        #endregion
                        if (records.Count>0)
                        {
                            #region Upload records
                                SaveResponse response = null;
                                try
                                {
                                    using (RideWCFServiceImplementation client = GetSyncClient())
                                    {
                                        RouteInstanceRequest request = new RouteInstanceRequest();
                                        //request.IMEINumber = Config.AppSettings.Ref.IMEINumber;
                                        request.GPSGateUser = Config.AppSettings.Ref.GPSGateUserID;
                                        request.RideCredentials = _rideCredential;
                                        request.RouteInstances = records.ToArray();
                                        response = client.SaveRouteInstances(request);
                                    }
                                }
                                catch (System.Web.Services.Protocols.SoapException ex)
                                {
                                    Console.Write(ex.Message);
                                }
                                catch (Exception ex)
                                {
                                    Console.Write(ex.Message);
                                }
                                finally
                                {
                                    if (response != null)
                                    {
                                        #region Save Route Instances Response
                                            Dictionary<string, int> ids = new Dictionary<string, int>();
                                            foreach (ArrayOfKeyValueOfguidintKeyValueOfguidint a in response.IDs)
                                            {
                                                ids.Add(a.Key, a.Value);
                                            }                            
                                            foreach (RouteInstance routeInstance in routeInstances)
                                            {
                                                int id;
                                                if (ids.TryGetValue(routeInstance.UniqueID,out id))
                                                {
                                                    Data.DataHolder.Ref.UpdateRouteInstanceID(routeInstance.UniqueID,id);
                                                    ids.Remove(routeInstance.UniqueID);
                                                }
                                            }
                                            //remove all edited records that were saved
                                            foreach (KeyValuePair<string, int> kvp in ids)
                                            {
                                                Data.DataHolder.Ref.ClearEditedRecord(kvp.Key);
                                            }
                                        #endregion
                                    }
                                }
                            #endregion
                        }
                    }
                }

            }
            public void SyncDVIRs(List<DVIR> dvirs)
            {
                SaveResponse response = null;
                if (dvirs.Count > 0)
                { 
                    try
                    {
                        using (RideWCFServiceImplementation client = GetSyncClient())
                        {
                            DVIRsSaveRequest request = new DVIRsSaveRequest();
                            request.RideCredentials = _rideCredential;
                            request.DVIRsToSave=dvirs.ToArray();
                            response = client.SaveDVIRs(request);
                        }
                    }
                    catch (System.Web.Services.Protocols.SoapException ex)
                    {
                        Console.Write(ex.Message);
                    }
                    catch (Exception ex)
                    {
                        Console.Write(ex.Message);
                    }
                    finally
                    {
                        if (response != null)
                        {
                            #region Save DVIR Response
                            Dictionary<string, int> ids = new Dictionary<string, int>();
                                foreach (ArrayOfKeyValueOfguidintKeyValueOfguidint a in response.IDs)
                                {
                                    ids.Add(a.Key, a.Value);
                                }
                                foreach (DVIR dvir in dvirs)
                                {
                                    int id;
                                    if (ids.TryGetValue(dvir.UniqueID, out id))
                                    {
                                        Data.DataHolder.Ref.UpdateDVIRID(dvir.UniqueID, id);
                                        ids.Remove(dvir.UniqueID);
                                    }
                                    foreach (DVIRDetail detail in dvir.Details)
                                    {
                                        foreach (Comment comment in detail.Comments)
                                        {
                                            if (ids.TryGetValue(comment.UniqueID, out id))
                                            {
                                                Data.DataHolder.Ref.UpdateDVIRDetailCommentID(comment.UniqueID, id);
                                                ids.Remove(comment.UniqueID);
                                            }
                                        }
                                    }
                                    //remove all edited records that were saved
                                    foreach (KeyValuePair<string, int> kvp in ids)
                                    {
                                        Data.DataHolder.Ref.ClearEditedRecord(kvp.Key);
                                    }
                                }
                            #endregion
                        }
                    }
                }
            }
            public void SyncHandheldComments(List<Comment> comments)
            {
                SaveResponse response = null;
                if (comments.Count > 0)
                {
                    try
                    {
                        using (RideWCFServiceImplementation client = GetSyncClient())
                        {
                            HandheldCommentsSyncRequest request = new HandheldCommentsSyncRequest();
                            request.RideCredentials = _rideCredential;
                            request.Comments = comments.ToArray();
                            response = client.SaveHandheldComments(request);
                        }
                    }
                    catch (System.Web.Services.Protocols.SoapException ex)
                    {
                        Console.Write(ex.Message);
                    }
                    catch (Exception ex)
                    {
                        Console.Write(ex.Message);
                    }
                    finally
                    {
                        if (response != null)
                        {
                            #region Save Comment Response
                                Dictionary<string, int> ids = new Dictionary<string, int>();                            
                                foreach (ArrayOfKeyValueOfguidintKeyValueOfguidint a in response.IDs)
                                {
                                    ids.Add(a.Key, a.Value);
                                }
                                foreach (Comment comment in comments)
                                {
                                    int id;
                                    if (ids.TryGetValue(comment.UniqueID, out id))
                                    {
                                        Data.DataHolder.Ref.UpdateHandheldCommentID(comment.UniqueID, id);
                                        ids.Remove(comment.UniqueID);
                                    }
                                    //remove all edited records that were saved
                                    foreach (KeyValuePair<string, int> kvp in ids)
                                    {
                                        Data.DataHolder.Ref.ClearEditedRecord(kvp.Key);
                                    }
                                }
                            #endregion
                        }
                    }
                }            
            }
            public void GetHandheldComments(IProgress progressUpdate, bool fullSync)
            {
                HandheldCommentsResponse response = null;
                GetHandheldCommentsRequest request = new GetHandheldCommentsRequest();
                request.DaysBack = Config.AppSettings.Ref.ProgramSettings.DaysToKeepHandheldComments;
                request.DaysBackSpecified = true;
                request.RideCredential = _rideCredential;
                request.Request = new RequestArgument();
                if (fullSync)
                {
                    request.Request.RequestType = RequestArgumentRequestTypes.FullSync;
                    request.Request.RequestTypeSpecified = true;
                    request.Request.DateSpecified = false;
                }
                else
                {
                    request.Request.RequestType = RequestArgumentRequestTypes.SyncFromDate;
                    request.Request.RequestTypeSpecified = true;
                    request.Request.Date = Data.DataHolder.Ref.GetSyncDate(DataHolder.SyncTypes.HandheldComments);
                    request.Request.DateSpecified = true;
                }
                try
                {
                    using (RideWCFServiceImplementation client = GetSyncClient())
                    {
                        response = client.GetHandheldComments(request);
                    }

                }
                catch (System.Web.Services.Protocols.SoapException ex)
                {
                    Console.Write(ex.Message);
                }
                catch (Exception ex)
                {
                    Console.Write(ex.Message);
                }
                finally
                {
                    if (response != null && response.Success)
                    {
                        #region Comments
                            progressUpdate.IncrementProgress();
                            if (response.Comments.Length > 0)
                            {
                                progressUpdate.SetStatus("Updating Handheld Comments");
                                if (request.Request.RequestType == RequestArgumentRequestTypes.FullSync)
                                    Data.DataHolder.Ref.ClearHandheldComments(true);
                                else
                                    Data.DataHolder.Ref.ClearHandheldComments(false);
                                foreach (Comment comment in response.Comments)
                                {
                                    Data.DataHolder.Ref.SaveHandheldComment(Converter.Comment(comment));
                                }
                            }
                        #endregion

                        #region Save Last Sync Date
                        if (response.ServerSyncTimeSpecified)
                            Data.DataHolder.Ref.SaveSyncDate(DataHolder.SyncTypes.HandheldComments, response.ServerSyncTime);
                        #endregion
                    }
                }

            }
            public bool SaveVehicleRoute(Ride.Business.Objects.Employee employee, Ride.Business.Objects.Vehicles.Vehicle vehicle, Ride.Business.Objects.Routes.Route route)
            {
                try
                {
                    using (RideWCFServiceImplementation client = GetSyncClient())
                    {

                        VehicleRoute request = new VehicleRoute();
                        SaveResponse response = null;
                        string gpsGateUser = string.Empty;
                        request.UniqueID = Guid.NewGuid().ToString();
                        request.VehicleID = vehicle.VehicleID;
                        request.VehicleIDSpecified = true;
                        if (employee == null)
                            request.PersonID = 0;
                        else
                            request.PersonID = employee.PersonID;
                        request.PersonIDSpecified = true;
                        if (route == null)
                            request.RouteID = 0;
                        else
                            request.RouteID = route.RouteID;
                        request.RouteIDSpecified = true;
                        //Only get the GPS Gate User if it is required. Otherwise, just move the vehicle/route...
                        if (Config.AppSettings.Ref.ProgramSettings.RequireGPSGateForVehicleRoute)
                        {
                            gpsGateUser = Config.AppSettings.Ref.GetGPSGateUser();
                            //If we don't have a GPS Gate User, then exit out because we dont have a user to update with
                            if (string.IsNullOrEmpty(gpsGateUser))
                                return false;
                            request.GpsGateUserName = gpsGateUser;
                            response = client.SaveVehicleRoute(_rideCredential, request);

                        }
                        else
                        {
                            response = client.MoveVehicleToRouteWithoutGPSGateUser(_rideCredential, request);
                        }
                        if (response == null)
                            return false;
                        return response.Success;

                    }
                }
                catch (System.Net.WebException ex)
                {
                    Console.Write(ex.Message);
                    return false;
                }
                catch (Exception ex)
                {

                    throw ex;
                }
            }
            public List<Ride.Business.Objects.DVIRs.DVIRWithPrompts> GetPreviousDVIRs(int vehicleID, int daysBack)
            {
                DVIRsResponse response = null;
                List<Ride.Business.Objects.DVIRs.DVIRWithPrompts> results = null;
                try
                {
                    using (RideWCFServiceImplementation client = GetSyncClient())
                    {
                        DVIRsRequest request = new DVIRsRequest();
                        request.DaysBack = daysBack;
                        request.DaysBackSpecified = true;
                        request.VehicleIDs = new int[] { vehicleID };
                        request.RideCredentials = _rideCredential;
                        response = client.GetHandheldDVIRs(request);
                    }
                }
                catch (System.Web.Services.Protocols.SoapException ex)
                {
                    Console.Write(ex.Message);
                }
                catch (Exception ex)
                {
                    Console.Write(ex.Message);
                }
                finally
                {
                    if (response != null)
                    {
                        if (response.Success)
                        {
                            results = new List<Ride.Business.Objects.DVIRs.DVIRWithPrompts>();
                            foreach(DVIRWithPrompts dvir in response.DVIRs)
                            {
                                results.Add(Converter.DVIRWithPrompts(dvir));
                            }
                        }
                    }
                }
                return results;
            }
        #endregion
    }
}
