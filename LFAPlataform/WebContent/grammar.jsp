<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link type="text/css" rel="stylesheet" href="css/MainStyle.css">
<title>Gram&aacute;tica</title>
</head>
<body>
	<div id="align">
		<form action="Grammar" method="post">
			S&iacute;mbolo inicial: <input type="text"	size="1" 	maxlength="1"	name="initialSymbol"> <br/> <br/>
			Vari&aacute;veis: <input type="text" size="10" maxlength="20"	name="variables"> <br/> <br/>
			Terminais: <input type="text" size="10" maxlength="20"	name="terminals"> <br/> <br/>
			Palavra(Apenas para CYK): <input type="text"		size="20" 	maxlength=""	name="word"> </br></br>
			Regras de produ&ccedil;&atilde;o: <br/>
			<div id="align-textarea">				
				<textarea rows="12" cols="50" name="txtGrammar"></textarea> <br/><br/>
			<div id="align-button"><input type="submit" name="action"	value="Símbolo inicial não recursivo" /></div>			
			<div id="align-button"><input type="submit" name="action"	value="Remoção de símbolo lâmbda" /></div>
			<div id="align-button"><input type="submit" name="action"	value="Remoção de regras da cadeia" /></div>
			<div id="align-button"><input type="submit" name="action"	value="Remoção de regras inalcançáveis" /></div>
			<div id="align-button"><input type="submit" name="action"	value="Remoção de regras não terminais" /></div>
			<div id="align-button"><input type="submit" name="action" 	value="Forma Normal de Chomsky" /></div>
			<div id="align-button"><input type="submit" name="action" 	value="Remoção de recursão à esquerda imediata" /></div>
			<div id="align-button"><input type="submit" name="action" 	value="Remoção de recursão à esquerda direta e indireta" /></div>
			<div id="align-button"><input type="submit" name="action" 	value="CYK" /></div>
			</div>		
		</form>
	</div>
</body>
</html>