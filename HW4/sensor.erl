% Taylor He
% I pledge my honor that I have abided by the Stevens Honor System.
-module(sensor).
-export([sense/2]).

sense(From, ID) ->
	Measurement = rand:uniform(11),
	if Measurement == 11 ->
		From!{ID, "anomalous_reading"},
		exit(anomalous_reading);
	true ->
		From!{ID, Measurement}
	end,
	Sleep_time = rand:uniform(10000),
	timer:sleep(Sleep_time),
	sense(From, ID).
