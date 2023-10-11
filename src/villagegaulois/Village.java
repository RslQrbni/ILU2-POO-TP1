package villagegaulois;

import java.util.ArrayList;
import java.util.List;

import personnages.Chef;
import personnages.Gaulois;

public class Village {
	private String nom;
	private Chef chef;
	private Gaulois[] villageois;
	private int nbVillageois = 0;
	private Marche marche;

	// ajouter int nbVillageois
	public Village(String nom, int nbVillageoisMaximum, int nbEtal) {
		this.nom = nom;
		villageois = new Gaulois[nbVillageoisMaximum];
		this.nbVillageois = 0;
		marche = new Marche(nbEtal);
	}

	public String getNom() {
		return nom;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}

	public void ajouterHabitant(Gaulois gaulois) {
		if (nbVillageois < villageois.length) {
			villageois[nbVillageois] = gaulois;
			nbVillageois++;
		}
	}

	public Gaulois trouverHabitant(String nomGaulois) {
		if (nomGaulois.equals(chef.getNom())) {
			return chef;
		}
		for (int i = 0; i < nbVillageois; i++) {
			Gaulois gaulois = villageois[i];
			if (gaulois.getNom().equals(nomGaulois)) {
				return gaulois;
			}
		}
		return null;
	}

	public String afficherVillageois() {
		StringBuilder chaine = new StringBuilder();
		if (nbVillageois < 1) {
			chaine.append("Il n'y a encore aucun habitant au village du chef " + chef.getNom() + ".\n");
		} else {
			chaine.append("Au village du chef " + chef.getNom() + " vivent les légendaires gaulois :\n");
			for (int i = 0; i < nbVillageois; i++) {
				chaine.append("- " + villageois[i].getNom() + "\n");
			}
		}
		return chaine.toString();
	}

	// creation de class marche
	private static class Marche {
		private Etal[] etals;

		private Marche(int nbEtals) {
			etals = new Etal[nbEtals];
			for (int i = 0; i < etals.length; i++) {
				etals[i] = new Etal();
			}

		}

		private void utiliserEtal(int indiceEtal, Gaulois vendeur, String produit, int nbProduit) {
			etals[indiceEtal].occuperEtal(vendeur, produit, nbProduit);
			// etals[indiceEtal].libererEtal();

		}

		int trouverEtalLibre() {
			int nbEtalLibre = 0;
			for (int i = 0; i < etals.length; i++) {
				if (!etals[i].isEtalOccupe())
					return i;
			}
			return -1;
		}

		Etal[] trouverEtals(String produit) {
			List<Etal> etalsVendantProduit = new ArrayList<>();

			for (int i = 0; i < etals.length; i++) {
				if (!etals[i].isEtalOccupe() && (etals[i].contientProduit(produit))) {
					etalsVendantProduit.add(etals[i]);
				}
			}

			return etalsVendantProduit.toArray(new Etal[0]);
		}

		// TrouverVendeur
		Etal trouverVendeur(Gaulois gaulois) {
			for (int i = 0; i < etals.length; i++) {
				if (etals[i] != null && etals[i].getVendeur() == gaulois) {
					return etals[i];
				}
			}
			return null;
		}

		private String afficherMarche() {
			StringBuilder builder = new StringBuilder();

			for (int i = 0; i < etals.length; i++) {
				if (etals[i].isEtalOccupe()) {
					builder.append(etals[i].afficherEtal());
				}
			}

			int nbEtalVide = 0;
			for (int i = 0; i < etals.length; i++) {
				if (!etals[i].isEtalOccupe()) {
					nbEtalVide++;
				}
			}

			if (nbEtalVide > 0) {
				builder.append("Il reste ").append(nbEtalVide).append(" étals non utilisés dans le marché.\n");
			}

			return builder.toString();
		}
	} // <= Marche

	// les Methode
	public String installerVendeur(Gaulois vendeur, String produit, int nbProduit) {
		StringBuilder builder = new StringBuilder();
		builder.append(vendeur.getNom()).append(" cherche un endroit pour vendre ").append(nbProduit).append(" ")
				.append(produit).append(".\n");

		int indiceEtal = marche.trouverEtalLibre();
		if (indiceEtal >= 0) {
			marche.utiliserEtal(indiceEtal, vendeur, produit, nbProduit);
			builder.append("Le vendeur ").append(vendeur.getNom()).append(" vend des ").append(produit)
					.append(" à l'étal n°").append(indiceEtal + 1).append(".\n");
		}

		return builder.toString();
	}

	// Recherche vendeur
	public String rechercherVendeursProduit(String produit) {
		List<String> vendeursProduit = new ArrayList<>();

		for (Etal etal : marche.trouverEtals(produit)) {
			Gaulois vendeur = etal.getVendeur();
			vendeursProduit.add(vendeur.getNom());
		}

		StringBuilder builder = new StringBuilder("Les vendeurs qui proposent des ").append(produit)
				.append(" sont :\n");

		for (String nomVendeur : vendeursProduit) {
			builder.append("- ").append(nomVendeur).append("\n");
		}

		return builder.toString();
	}

	// recherche etals
	public Etal rechercherEtal(Gaulois vendeur) {
		return marche.trouverVendeur(vendeur);
	}

	// partir vendeur
	public String partirVendeur(Gaulois vendeur) {
		Etal etal = marche.trouverVendeur(vendeur);
		if (etal != null) {
			return etal.libererEtal();
		} else {
			return "";
		}
	}

	public String afficherMarche() {
		return marche.afficherMarche();
	}

}