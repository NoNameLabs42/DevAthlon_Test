package de.NoNameLabs.Main;

public enum Note {
	Fis(0.0f),G(0.53f),Gis(0.56f),A(0.6f),Ais(0.63f),H(0.67f),C(0.7f),Cis(0.75f),D(0.8f),Dis(0.85f),E(0.9f),F(0.95f),Fis2(1.0f),G2(1.05f),Gis2(1.1f),A2(1.2f),Ais2(1.25f),
	H2(1.32f),C2(1.4f),Cis2(1.5f),D2(1.6f),Dis2(1.7f),E2(1.8f),F2(1.9f),Fis3(2.0f);
	
	
	private float pitch;
	
	Note(float pitch)  {
		this.pitch = pitch;
	}
	
	
	public float getPitch() {
		return pitch;
	}
}
