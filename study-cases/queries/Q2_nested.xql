(: Os codigos e setores das quadras da região oeste :)

<blocks>
	LET $doc := document("city.xml")
	FOR $r in $doc//region
	WHERE $r/name = "oeste"
	RETURN
	<block>
		<code>{$r/aggregationRN/neighborhood/containsNB/block/code/text()}</code>
		<sector>{$r/aggregationRN/neighborhood/containsNB/block/sector/text()}</sector>
	</block>
<blocks>