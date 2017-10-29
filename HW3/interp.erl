-module(interp).
-export([scanAndParse/1,runFile/1,runStr/1]).
-include("types.hrl").

loop(InFile,Acc) ->
    case io:request(InFile,{get_until,prompt,lexer,token,[1]}) of
        {ok,Token,_EndLine} ->
            loop(InFile,Acc ++ [Token]);
        {error,token} ->
            exit(scanning_error);
        {eof,_} ->
            Acc
    end.

scanAndParse(FileName) ->
    {ok, InFile} = file:open(FileName, [read]),
    Acc = loop(InFile,[]),
    file:close(InFile),
    {Result, AST} = parser:parse(Acc),
    case Result of 
	ok -> AST;
	_ -> io:format("Parse error~n")
    end.


-spec runFile(string()) -> valType().
runFile(FileName) ->
    valueOf(scanAndParse(FileName),env:new()).

scanAndParseString(String) ->
    {_ResultL, TKs, _L} = lexer:string(String),
    parser:parse(TKs).

-spec runStr(string()) -> valType().
runStr(String) ->
    {Result, AST} = scanAndParseString(String),
    case Result  of 
    	ok -> valueOf(AST,env:new());
    	_ -> io:format("Parse error~n")
    end.


-spec numVal2Num(numValType()) -> integer().
numVal2Num({num, N}) ->
    N.

-spec boolVal2Bool(boolValType()) -> boolean().
boolVal2Bool({bool, B}) ->
    B.

-spec valueOf(expType(),envType()) -> valType().

% If it's a numVal or a boolBal, return itself.
valueOf({num, N}, _Env) ->
    {num, N};

% If it's an Id Exp, lookup what it is
valueOf({idExp, {id, _, Id}}, Env) ->
    env:lookup(Env, Id);

% If it's a diffExp, calculate recursively and use numVal2Num defined above.
valueOf({diffExp, Exp1, Exp2}, Env) ->
    N = numVal2Num(valueOf(Exp1, Env)) - numVal2Num(valueOf(Exp2, Env)),
    {num, N};

% If it's a plusExp, calculate recursively and use numVal2Num defined above.
valueOf({plusExp, Exp1, Exp2}, Env) ->
    N = numVal2Num(valueOf(Exp1, Env)) + numVal2Num(valueOf(Exp2, Env)),
    {num, N};

% If it's a isZeroExp, calculate recursively and use numVal2Num defined above.
valueOf({isZeroExp, Exp}, Env) ->
    Val = (numVal2Num(valueOf(Exp, Env)) == 0),
    {bool, Val};

% ifThenElseExp has some pattern matching
valueOf({ifThenElseExp, Exp1, Exp2, Exp3}, Env) ->
    if Exp1 == true ->
           valueOf(Exp2, Env);
       Exp1 == false ->
           valueOf(Exp3, Env);
       true ->
           valueOf({ifThenElseExp, boolVal2Bool(valueOf(Exp1, Env)), Exp2, Exp3}, Env);
    end;

% letExp has an Identifier which we need to store in the Env
valueOf({letExp, IdVal, ValExp, InExp}, Env) ->
    {id, _, Id} = IdVal,
    valueOf(InExp, env:add(Env, Id, valueOf(ValExp, Env)));





















