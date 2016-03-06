
public class OMTGRelationship extends Relationship {

	private String side;
	private String entityA, entityB;
	private OMTGCardinality cardA, cardB, card;
	private String cardRelationA, cardRelationB;

	public OMTGRelationship(String name, String type, String entityA, String entityB, OMTGCardinality cardA, OMTGCardinality cardB) {
		super(name, type);
		this.entityA = entityA;
		this.entityB = entityB;
		this.cardA = cardA;
		this.cardB = cardB;
		if (cardA != null && cardB != null) {
			this.cardRelationA = cardB.getMax();
			this.cardRelationB = cardA.getMax();
			this.card = new OMTGCardinality(cardRelationA, cardRelationB);
		}
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public String getEntityA() {
		return entityA;
	}

	public String getEntityB() {
		return entityB;
	}

	public OMTGCardinality getCardA() {
		return cardA;
	}

	public OMTGCardinality getCardB() {
		return cardB;
	}

	public int getParticipationCardA() {
		return cardA.getParticipation();
	}

	public int getParticipationCardB() {
		return cardB.getParticipation();
	}

	public boolean isParticipationCardAPartial() {
		return getParticipationCardA() == 0;
	}

	public boolean isParticipationCardBPartial() {
		return getParticipationCardB() == 0;
	}

	public boolean isParticipationCardATotal() {
		return getParticipationCardA() == 1;
	}

	public boolean isParticipationCardBTotal() {
		return getParticipationCardB() == 1;
	}

	public String getCardRelationA() {
		return cardRelationA;
	}

	public String getCardRelationB() {
		return cardRelationB;
	}

	public OMTGCardinality getCard() {
		return card;
	}

	public String toString() {
		return "(" + cardRelationA + "," + cardRelationB + ")";
	}

}
