-module(sensor).
-compile(export_all).

sensor(From, Id) ->
	Measurement = rand:uniform(11),
	if Measurement == 11 ->
		exit({anomalous_reading});
	true ->
		From!{Id, Measurement}
	end,
	Sleep_time = rand:uniform(10000),
	timer:sleep(Sleep_time),
	sensor(From, Id).
