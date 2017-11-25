-module(watcher).
-compile(export_all).

% Make WatchNum watchers with 10 per watcher
new(WatchNum) ->
	new_spawn(WatchNum, 0).

new_spawn(WatchNum, CurrNum) ->
	if WatchNum < 10 ->
        spawn(?MODULE, watcher, [WatchNum, CurrNum]);
    true ->
        spawn(?MODULE, watcher, [10, CurrNum]),
    	new_spawn(WatchNum - 10, CurrNum + 10)
    end.
%        
watcher(WatchNum, CurrNum)->
    PidList = spawn_sensors(WatchNum, CurrNum, []),
    io:fwrite("Starting watcher: ~p~n", [PidList]),
    watcher_loop(PidList).

watcher_loop(PidList) ->
    receive
        % If anomalous reading, restart Sensor with SensorID: ID
        {ID, "anomalous_reading"} ->
            io:fwrite("Sensor ~p has crashed due to: ~p... Restarting...~n", [ID, "anomalous_reading"]);
            
        {ID, Measurement} ->
            io:fwrite("Sensor ~p reported a measurement of: ~p~n",[ID, Measurement])
    end,
    watcher_loop(PidList).
% Spawn the sensors using spawn_monitor
spawn_sensors(NumSensors, CurrNum, PidList) ->
    if NumSensors == 0 ->
        PidList;
    true ->
        {Pid, _} = spawn_monitor(sensor, sense, [self(), CurrNum]),
        spawn_sensors(NumSensors - 1, CurrNum + 1, PidList ++ [{CurrNum, Pid}])
    end.
