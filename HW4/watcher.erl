-module(watcher).
-export([new/1]).

% User uses this
new(WatchNum) ->
	new_spawn(WatchNum, 0).

% Make WatchNum watchers with 10 per watcher
new_spawn(WatchNum, CurrNum) when WatchNum < 10 ->
    spawn(watcher, watcher, [WatchNum, CurrNum]);
new_spawn(WatchNum, CurrNum) ->
    spawn(watcher, watcher, [10, CurrNum]),
    new_spawn(WatchNum - 10, CurrNum + 10).

% Bad form! Use pattern matching.
% new_spawn(WatchNum, CurrNum) ->
% 	if WatchNum < 10 ->
%         spawn(?MODULE, watcher, [WatchNum, CurrNum]);
%     true ->
%         spawn(?MODULE, watcher, [10, CurrNum]),
%     	  new_spawn(WatchNum - 10, CurrNum + 10)
%     end.

% Function that passes a generated PidList to the loop function
watcher(WatchNum, CurrNum)->
    PidList = spawn_sensors(WatchNum, CurrNum, []),
    io:fwrite("Starting watcher: ~p~n", [PidList]),
    watcher_loop(PidList).

% Receive and handle errors
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
spawn_sensors(0, CurrNum, PidList) ->
    PidList;
spawn_sensors(NumSensors, CurrNum, PidList) ->
    {Pid, _} = spawn_monitor(sensor, sense, [self(), CurrNum]),
    spawn_sensors(NumSensors-1, CurrNum + 1, PidList ++ [{CurrNum, Pid}]).

% Bad Form! Do pattern matching instead.
% spawn_sensors(NumSensors, CurrNum, PidList) ->
%     if NumSensors == 0 ->
%         PidList;
%     true ->
%         {Pid, _} = spawn_monitor(sensor, sense, [self(), CurrNum]),
%         spawn_sensors(NumSensors - 1, CurrNum + 1, PidList ++ [{CurrNum, Pid}])
%     end.
