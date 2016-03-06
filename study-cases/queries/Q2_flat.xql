(: Os codigos e setores das quadras da região oeste :)

<blocks>
	LET $doc := document("city.xml")
	FOR $r in $doc//region,
		   $n in $doc//neighborhood,
		   $b in $doc//block
	WHERE $r/name = "oeste" AND $r/aggregationRN/@ref = $n/name AND $n/containsNB/@ref = $b/code
	RETURN
	<block>
		<code>{$b/code/text()}</code>
		<sector>{$b/sector/text()}</sector>
	</block>
</blocks>