% Taylor He
% I pledge my honor that I have abided by the Stevens Honor System.
-module(watcher).
-import(sensor, [sense/2]).
-export([new/1, watcher/2, watcher_loop/2]).

% User calls this!
new(WatchNum) ->
	new_spawn(WatchNum, 0).

% Make WatchNum watchers with up to 10 per watcher.
new_spawn(WatchNum, CurrNum) when WatchNum < 10 ->
    spawn(watcher, watcher, [WatchNum, CurrNum]);
new_spawn(WatchNum, CurrNum) ->
    spawn(watcher, watcher, [10, CurrNum]),
    new_spawn(WatchNum - 10, CurrNum + 10).

% Function that passes a generated PidList to the loop function
watcher(WatchNum, CurrNum)->
    PidList = spawn_sensors(WatchNum, CurrNum, []),
    io:fwrite("Starting watcher ~p with list of sensors as {SensorID, PID}: ~p~n", [self(), PidList]),
    watcher_loop(self(), PidList).

% Spawn the sensors using spawn_monitor
spawn_sensors(0, _, PidList) ->
    PidList;
spawn_sensors(NumSensors, CurrNum, PidList) ->
    {Pid, _} = spawn_monitor(sensor, sense, [self(), CurrNum]),
    spawn_sensors(NumSensors-1, CurrNum + 1, PidList ++ [{CurrNum, Pid}]).

% Watcher Loop that recieves sensor measurements and handles crashes by restarting
watcher_loop(WID, PidList) ->
    receive
        % If anomalous reading, restart Sensor with SensorID: ID
        {SensorID, "anomalous_reading"} ->
            io:fwrite("Sensor ~p has crashed due to: ~p. Restarting...~n", [SensorID, "anomalous_reading"]),
            {PID, _} = spawn_monitor(sensor, sense, [self(), SensorID]),
            % In the Erlang Docs keyreplace(Key, N, TupleList1, NewTuple) Returns a copy of TupleList1 where 
            % the first occurrence of a T tuple whose Nth element is equal to Key is replaced with NewTuple, 
            % if there is such a tuple T.
            NewPidList = lists:keyreplace(SensorID, 1, PidList, {SensorID, PID}),
            io:fwrite("Watcher ~p has new sensor list ~p~n", [WID, NewPidList]);
        {SensorID, Measurement} ->
            io:fwrite("Sensor ~p reported a measurement of: ~p~n",[SensorID, Measurement]),
            NewPidList = PidList
    end,
    watcher_loop(WID, NewPidList).


% Bad form! Use pattern matching.
% new_spawn(WatchNum, CurrNum) ->
%   if WatchNum < 10 ->
%         spawn(?MODULE, watcher, [WatchNum, CurrNum]);
%     true ->
%         spawn(?MODULE, watcher, [10, CurrNum]),
%         new_spawn(WatchNum - 10, CurrNum + 10)
%     end.

% Bad Form! Do pattern matching instead.
% spawn_sensors(NumSensors, CurrNum, PidList) ->
%     if NumSensors == 0 ->
%         PidList;
%     true ->
%         {Pid, _} = spawn_monitor(sensor, sense, [self(), CurrNum]),
%         spawn_sensors(NumSensors - 1, CurrNum + 1, PidList ++ [{CurrNum, Pid}])
%     end.
