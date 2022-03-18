package p3d4amb.algorithms;

import static p3d4amb.algorithms.ThresholdCertifier.Result.CONTINUE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import com.opencsv.CSVWriter;

import p3d4amb.algorithms.ThresholdCertifier.Solution;
import p3d4amb.algorithms.comparator.BestNSim;
import p3d4amb.algorithms.comparator.ElementsType;
import p3d4amb.algorithms.comparator.Patient;
import p3d4amb.algorithms.comparator.PestNSim;
import p3d4amb.algorithms.comparator.PestSim;
import p3d4amb.algorithms.comparator.StrictSim;

public class TestComparatorTestRetestMultiLevel {

	static final int numpatients = 3000;
	static final int startingLevel = 15;

	static double probExpectedAnsw = 0.9;
	static double probExpectedAnswNotCert = 0.75;

	static final double probExpectedAnswAttentionDec = 0.9;
	static final double probExpectedAnswNotCertAttentionDec = 0.75;
	static final double stepAttentionDec = 8;

	private static void runStrict(int patientLevelCert, StrictSim strict, boolean attention) {
		if (attention) {
			int countSteps = 0;
			do {
				countSteps++;
				if (countSteps <= stepAttentionDec)
					strictStep(patientLevelCert, strict, probExpectedAnswNotCert, probExpectedAnsw);
				else
					strictStep(patientLevelCert, strict, probExpectedAnswNotCertAttentionDec,
							probExpectedAnswAttentionDec);
			} while (strict.getDp().getCurrentStatus().currentResult.equals(CONTINUE));
		} else {
			do {
				strictStep(patientLevelCert, strict, probExpectedAnswNotCert, probExpectedAnsw);
			} while (strict.getDp().getCurrentStatus().currentResult.equals(CONTINUE));
		}
	}

	private static void strictStep(int patientLevelCert, StrictSim strict, double probexpectedanswnotcert,
			double probexpectedansw) {
		ThresholdCertifier.Solution solution;
		// patient not certified
		if (patientLevelCert == 0)
			solution = getSolutionRandom(Solution.WRONG, probexpectedanswnotcert);
		else {
			if (strict.getDp().getCurrentThreshold() >= patientLevelCert)
				solution = getSolutionRandom(Solution.RIGHT, probexpectedansw);
			else
				solution = getSolutionRandom(Solution.WRONG, probexpectedansw);
		}
		strict.getDp().computeNextThreshold(solution);
		strict.setStepsStrict(strict.getStepsStrict() + 1);
	}

	private static void runBestN(int patientLevelCert, BestNSim bestN, boolean attention) {
		if (attention) {
			int countSteps = 0;
			do {
				countSteps++;
				if (countSteps <= stepAttentionDec)
					bestNStep(patientLevelCert, bestN, probExpectedAnswNotCert, probExpectedAnsw);
				else
					bestNStep(patientLevelCert, bestN, probExpectedAnswNotCertAttentionDec,
							probExpectedAnswAttentionDec);
			} while (bestN.getDp().getCurrentStatus().currentResult.equals(CONTINUE));
		} else {

			do {
				bestNStep(patientLevelCert, bestN, probExpectedAnswNotCert, probExpectedAnsw);
			} while (bestN.getDp().getCurrentStatus().currentResult.equals(CONTINUE));
		}
	}

	private static void bestNStep(int patientLevelCert, BestNSim bestN, double probexpectedanswnotcert,
			double probexpectedansw) {
		ThresholdCertifier.Solution solution;
		// patient not certified
		if (patientLevelCert == 0)
			solution = getSolutionRandom(Solution.WRONG, probexpectedanswnotcert);
		else {
			if (bestN.getDp().getCurrentThreshold() >= patientLevelCert)
				solution = getSolutionRandom(Solution.RIGHT, probexpectedansw);
			else
				solution = getSolutionRandom(Solution.WRONG, probexpectedansw);
		}
		bestN.getDp().computeNextThreshold(solution);
		bestN.setStepsBestN(bestN.getStepsBestN() + 1);
	}

	private static void runPestN(int patientLevelCert, PestNSim pestN) {
		do {
			pestNStep(patientLevelCert, pestN, probExpectedAnswNotCert, probExpectedAnsw);
		} while (pestN.getDp().getCurrentStatus().currentResult.equals(CONTINUE));
	}

	private static void runPestNAttention(int patientLevelCert, PestNSim pestN) {
		int countSteps = 0;
		do {
			countSteps++;
			if (countSteps <= stepAttentionDec)
				pestNStep(patientLevelCert, pestN, probExpectedAnswNotCert, probExpectedAnsw);
			else
				pestNStep(patientLevelCert, pestN, probExpectedAnswNotCertAttentionDec, probExpectedAnswAttentionDec);
		} while (pestN.getDp().getCurrentStatus().currentResult.equals(CONTINUE));
	}

	private static void pestNStep(int patientLevelCert, PestNSim pestN, double probexpectedanswnotcert,
			double probexpectedansw) {
		ThresholdCertifier.Solution solution;
		// patient not certified
		if (patientLevelCert == 0)
			solution = getSolutionRandom(Solution.WRONG, probexpectedanswnotcert);
		else {
			if (pestN.getDp().getCurrentThreshold() >= patientLevelCert)
				solution = getSolutionRandom(Solution.RIGHT, probexpectedansw);
			else
				solution = getSolutionRandom(Solution.WRONG, probexpectedansw);
		}
		pestN.getDp().computeNextThreshold(solution);
		pestN.setStepsPestN(pestN.getStepsPestN() + 1);
	}

	private static void runPest(int patientLevelCert, PestSim pest) {
		do {
			pestStep(patientLevelCert, pest, probExpectedAnswNotCert, probExpectedAnsw);
		} while (pest.getDp().getCurrentStatus().currentResult.equals(CONTINUE));
	}

	private static void runPestAttention(int patientLevelCert, PestSim pest) {
		int countSteps = 0;
		do {
			countSteps++;
			if (countSteps <= stepAttentionDec)
				pestStep(patientLevelCert, pest, probExpectedAnswNotCert, probExpectedAnsw);
			else
				pestStep(patientLevelCert, pest, probExpectedAnswNotCertAttentionDec, probExpectedAnswAttentionDec);
		} while (pest.getDp().getCurrentStatus().currentResult.equals(CONTINUE));
	}

	private static void pestStep(int patientLevelCert, PestSim pest, double probexpectedanswnotcert,
			double probexpectedansw) {
		ThresholdCertifier.Solution solution;
		// patient not certified
		if (patientLevelCert == 0)
			solution = getSolutionRandom(Solution.WRONG, probexpectedanswnotcert);
		else {
			if (pest.getDp().getCurrentThreshold() >= patientLevelCert)
				solution = getSolutionRandom(Solution.RIGHT, probexpectedansw);
			else
				solution = getSolutionRandom(Solution.WRONG, probexpectedansw);
		}
		pest.getDp().computeNextThreshold(solution);
		pest.setStepsPest(pest.getStepsPest() + 1);
	}

	/**
	 * Generate a random answer between RIGHT and WRONG
	 */
	static Solution getSolutionRandom(Solution desiredSol, double d) {
		Random r = new Random();
		double rand = r.nextDouble();
		if (rand < d) {
			return desiredSol;
		} else {
			if (desiredSol.equals(Solution.RIGHT))
				return Solution.WRONG;
			else
				return Solution.RIGHT;
		}

	}

	/**
	 * Save data to Excel file. Each row is composed by: "idPatient", "stepPest1",
	 * "stepPest2", "steppestN1", "steppestN2", "stepbestN1", "stepbestN2",
	 * "stepStrict1", "stepStrict2", "targetLevel", "levelPest1", "levelPest2",
	 * "levelpestN1", "levelpestN2", "levelbestN1", "levelbestN2", "levelStrict1",
	 * "levelStrict2", "resPest1", "resPest2", "respestN1", "respestN2",
	 * "resbestN1", "resbestN2", "resStrict1", "resStrict2" because of test retest
	 *
	 * @param workbook
	 */
	static void saveToExcel(Workbook workbook, List<ElementsType> results, int startingLevel) throws IOException {
		// Create a Workbook
		// Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating
		// `.xls` file

		// Create a Sheet
		Sheet sheet = workbook.createSheet("Simulation" + startingLevel);

		// Create a Font for styling header cells
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.RED.getIndex());

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);

		// Create header Row
		Row headerRow = sheet.createRow(0);
		String[] columns = { "idPatient", "stepPest1", "stepPest2", "steppestN1", "steppestN2", "stepbestN1",
				"stepbestN2", "stepStrict1", "stepStrict2", "targetLevel", "levelPest1", "levelPest2", "levelpestN1",
				"levelpestN2", "levelbestN1", "levelbestN2", "levelStrict1", "levelStrict2", "resPest1", "resPest2",
				"respestN1", "respestN2", "resbestN1", "resbestN2", "resStrict1", "resStrict2" };
		/*
		 * String[] columns = { "idPatient", "steppestN1", "steppestN2", "stepbestN1",
		 * "stepbestN2", "stepStrict1", "stepStrict2", "targetLevel", "levelpestN1",
		 * "levelpestN2", "levelbestN1", "levelbestN2", "levelStrict1", "levelStrict2",
		 * "respestN1", "respestN2", "resbestN1", "resbestN2", "resStrict1",
		 * "resStrict2" };
		 */// Create cells
		for (int i = 0; i < columns.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(columns[i]);
			cell.setCellStyle(headerCellStyle);
		}

		// Create Cell Style for formatting not certified patients. The value of target
		// level is colored in red
		Font notCertFont = workbook.createFont();
		notCertFont.setBold(true);
		notCertFont.setColor(IndexedColors.RED.getIndex());
		CellStyle notCertCellStyle = workbook.createCellStyle();
		notCertCellStyle.setFont(notCertFont);

		// Create Other rows and cells with simulation data
		int rowNum = 1;
		for (ElementsType singleres : results) {
			Row row = sheet.createRow(rowNum++);
			int cellnum = 0;
			row.createCell(cellnum++).setCellValue(singleres.getIdPatient());
			row.createCell(cellnum++).setCellValue(singleres.getPest1().getStepsPest());
			row.createCell(cellnum++).setCellValue(singleres.getPest2().getStepsPest());
			row.createCell(cellnum++).setCellValue(singleres.getPestN1().getStepsPestN());
			row.createCell(cellnum++).setCellValue(singleres.getPestN2().getStepsPestN());
			row.createCell(cellnum++).setCellValue(singleres.getBestN1().getStepsBestN());
			row.createCell(cellnum++).setCellValue(singleres.getBestN2().getStepsBestN());
			row.createCell(cellnum++).setCellValue(singleres.getStrict1().getStepsStrict());
			row.createCell(cellnum++).setCellValue(singleres.getStrict2().getStepsStrict());
			// if not cert set color to red and set target level at starting level value
			int targetcellnum = cellnum++;
			if (singleres.getTargetLevel() == 0) {
				Cell dateOfBirthCell = row.createCell(targetcellnum);
				dateOfBirthCell.setCellValue(singleres.getTargetLevel());
				dateOfBirthCell.setCellStyle(notCertCellStyle);
			} else
				row.createCell(targetcellnum).setCellValue(singleres.getTargetLevel());

			row.createCell(cellnum++).setCellValue(singleres.getPest1().getDp().getCurrentThreshold());
			row.createCell(cellnum++).setCellValue(singleres.getPest2().getDp().getCurrentThreshold());
			row.createCell(cellnum++).setCellValue(singleres.getPestN1().getDp().getCurrentThreshold());
			row.createCell(cellnum++).setCellValue(singleres.getPestN2().getDp().getCurrentThreshold());
			row.createCell(cellnum++).setCellValue(singleres.getBestN1().getDp().getCurrentThreshold());
			row.createCell(cellnum++).setCellValue(singleres.getBestN2().getDp().getCurrentThreshold());
			row.createCell(cellnum++).setCellValue(singleres.getStrict1().getDp().getCurrentThreshold());
			row.createCell(cellnum++).setCellValue(singleres.getStrict2().getDp().getCurrentThreshold());
			row.createCell(cellnum++)
					.setCellValue(singleres.getPest1().getDp().getCurrentStatus().currentResult.toString());
			row.createCell(cellnum++)
					.setCellValue(singleres.getPest2().getDp().getCurrentStatus().currentResult.toString());
			row.createCell(cellnum++)
					.setCellValue(singleres.getPestN1().getDp().getCurrentStatus().currentResult.toString());
			row.createCell(cellnum++)
					.setCellValue(singleres.getPestN2().getDp().getCurrentStatus().currentResult.toString());
			row.createCell(cellnum++)
					.setCellValue(singleres.getBestN1().getDp().getCurrentStatus().currentResult.toString());
			row.createCell(cellnum++)
					.setCellValue(singleres.getBestN2().getDp().getCurrentStatus().currentResult.toString());
			row.createCell(cellnum++)
					.setCellValue(singleres.getStrict1().getDp().getCurrentStatus().currentResult.toString());
			row.createCell(cellnum++)
					.setCellValue(singleres.getStrict2().getDp().getCurrentStatus().currentResult.toString());
		}

		// Resize all columns to fit the content size
		for (int i = 0; i < columns.length; i++) {
			sheet.autoSizeColumn(i);
		}

	}

	@Test
	public void testRetest() throws IOException {
		ArrayList<Patient> patientlist = new ArrayList<>();

		// Create a Workbook
		Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

		for (int j = startingLevel; j >= 5; j--) {
			// Generate X Patients with random level certified, at each for loop the maximum
			// level certified decreases
			patientlist.clear();
			for (int i = 0; i < numpatients; i++)
				patientlist.add(new Patient(j));
			// Array to store data and save them to DB
			List<ElementsType> results = new ArrayList<>();
			for (int i = 0; i < numpatients; i++) {
				// PestDepthCertifier
				PestSim pest1 = new PestSim(j);
				runPest(patientlist.get(i).getLevelCert(), pest1);
				PestSim pest2 = new PestSim(j);
				runPest(patientlist.get(i).getLevelCert(), pest2);

				// PestDepthCertifierNew
				PestNSim pestN1 = new PestNSim(j);
				runPestN(patientlist.get(i).getLevelCert(), pestN1);
				PestNSim pestN2 = new PestNSim(j);
				runPestN(patientlist.get(i).getLevelCert(), pestN2);

				// bestNepthCertifier
				BestNSim bestN1 = new BestNSim(j);
				runBestN(patientlist.get(i).getLevelCert(), bestN1, false);
				BestNSim bestN2 = new BestNSim(j);
				runBestN(patientlist.get(i).getLevelCert(), bestN2, false);

				// StrictStaircaseDepthCertifier
				StrictSim strict1 = new StrictSim(j);
				runStrict(patientlist.get(i).getLevelCert(), strict1, false);
				StrictSim strict2 = new StrictSim(j);
				runStrict(patientlist.get(i).getLevelCert(), strict2, false);

				results.add(new ElementsType(patientlist.get(i).getId(), patientlist.get(i).getLevelCert(), pest1,
						pest2, pestN1, pestN2, bestN1, bestN2, strict1, strict2));
				// results.add(new ElementsType(patientlist.get(i).getId(),
				// patientlist.get(i).getLevelCert(), pestN1, pestN2, bestN1, bestN2,
				// strict1, strict2));
			}
			// Save data to excel file
			saveToExcel(workbook, results, j);
		}
		// Write the output to a file
		FileOutputStream fileOut = new FileOutputStream(
				"simulationTestRetestMulti" + System.currentTimeMillis() + ".xlsx");
		workbook.write(fileOut);
		fileOut.close();

		// Closing the workbook
		workbook.close();
	}

	@Test
	public void testRetestAttentionDec() throws IOException {
		ArrayList<Patient> patientlist = new ArrayList<>();

		// Create a Workbook
		Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

		for (int j = startingLevel; j >= 5; j--) {
			// Generate X Patients with random level certified, at each for loop the maximum
			// level certified decreases
			patientlist.clear();
			for (int i = 0; i < numpatients; i++)
				patientlist.add(new Patient(j));
			// Array to store data and save them to DB
			List<ElementsType> results = new ArrayList<>();
			for (int i = 0; i < numpatients; i++) {
				// PestDepthCertifier
				PestSim pest1 = new PestSim(j);
				runPestAttention(patientlist.get(i).getLevelCert(), pest1);
				PestSim pest2 = new PestSim(j);
				runPestAttention(patientlist.get(i).getLevelCert(), pest2);

				// PestDepthCertifierNew
				PestNSim pestN1 = new PestNSim(j);
				runPestNAttention(patientlist.get(i).getLevelCert(), pestN1);
				PestNSim pestN2 = new PestNSim(j);
				runPestNAttention(patientlist.get(i).getLevelCert(), pestN2);

				// bestNepthCertifier
				BestNSim bestN1 = new BestNSim(j);
				runBestN(patientlist.get(i).getLevelCert(), bestN1, true);
				BestNSim bestN2 = new BestNSim(j);
				runBestN(patientlist.get(i).getLevelCert(), bestN2, true);

				// StrictStaircaseDepthCertifier
				StrictSim strict1 = new StrictSim(j);
				runStrict(patientlist.get(i).getLevelCert(), strict1, true);
				StrictSim strict2 = new StrictSim(j);
				runStrict(patientlist.get(i).getLevelCert(), strict2, true);

				results.add(new ElementsType(patientlist.get(i).getId(), patientlist.get(i).getLevelCert(), pest1,
						pest2, pestN1, pestN2, bestN1, bestN2, strict1, strict2));
				// results.add(new ElementsType(patientlist.get(i).getId(),
				// patientlist.get(i).getLevelCert(), pestN1, pestN2, bestN1, bestN2,
				// strict1, strict2));
			}
			// Save data to excel file
			saveToExcel(workbook, results, j);
		}
		// Write the output to a file
		FileOutputStream fileOut = new FileOutputStream(
				"simulationTestRetestMulti" + System.currentTimeMillis() + ".xlsx");
		workbook.write(fileOut);
		fileOut.close();

		// Closing the workbook
		workbook.close();
	}

	enum Scenario {
		S0, S1, S2, S3
	}

	@Test
	public void testRetestCSV() throws IOException {
		ArrayList<Patient> patientlist = new ArrayList<>();

		String filePath = "TestRetest" + System.currentTimeMillis() + ".csv";
		File file = new File(filePath);
		// Create a Workbook
		FileWriter outputfile = new FileWriter(file);
		CSVWriter writer = new CSVWriter(outputfile);
		String[] header = { "Scenario", "IdPatient", "Target", "TestType", "Time", "Steps", "Level", "FinalRes" };
		writer.writeNext(header);
		for (Scenario s : Scenario.values()) {
			switch (s) {
			case S0:
				probExpectedAnsw = 1;
				probExpectedAnswNotCert = 1;
				break;
			case S1:
				probExpectedAnsw = 0.9;
				probExpectedAnswNotCert = 0.9;
				break;
			case S2:
				probExpectedAnsw = 0.9;
				probExpectedAnswNotCert = 0.75;
				break;
			case S3:
				probExpectedAnsw = 0.9;
				probExpectedAnswNotCert = 0.9;
				break;
			}			
			for (int j = startingLevel; j >= startingLevel; j--) {
				// Generate X Patients with random level certified, at each for loop the maximum
				// level certified decreases
				patientlist.clear();
				for (int i = 0; i < numpatients; i++)
					patientlist.add(new Patient(j));
				// Array to store data and save them to DB
				List<ElementsType> results = new ArrayList<>();
				for (int i = 0; i < numpatients; i++) {
					// PestDepthCertifier
					PestSim pest1 = new PestSim(j);
					runPest(patientlist.get(i).getLevelCert(), pest1);
					PestSim pest2 = new PestSim(j);
					runPest(patientlist.get(i).getLevelCert(), pest2);
					writer.writeNext(new String[] { s.name(), String.valueOf(i),
							String.valueOf(patientlist.get(i).getLevelCert()), "PEST", String.valueOf(0),
							String.valueOf(pest1.getStepsPest()), String.valueOf(pest1.getDp().getCurrentThreshold()),
							String.valueOf(pest1.getDp().getCurrentStatus().currentResult.toString()) });
					writer.writeNext(new String[] { s.name(), String.valueOf(i),
							String.valueOf(patientlist.get(i).getLevelCert()), "PEST", String.valueOf(1),
							String.valueOf(pest2.getStepsPest()), String.valueOf(pest2.getDp().getCurrentThreshold()),
							String.valueOf(pest2.getDp().getCurrentStatus().currentResult.toString()) });

					// PestDepthCertifierNew
					PestNSim pestN1 = new PestNSim(j);
					runPestN(patientlist.get(i).getLevelCert(), pestN1);
					PestNSim pestN2 = new PestNSim(j);
					runPestN(patientlist.get(i).getLevelCert(), pestN2);
					writer.writeNext(new String[] { s.name(), String.valueOf(i),
							String.valueOf(patientlist.get(i).getLevelCert()), "PESTN", String.valueOf(0),
							String.valueOf(pestN1.getStepsPestN()),
							String.valueOf(pestN1.getDp().getCurrentThreshold()),
							String.valueOf(pestN1.getDp().getCurrentStatus().currentResult.toString()) });
					writer.writeNext(new String[] { s.name(), String.valueOf(i),
							String.valueOf(patientlist.get(i).getLevelCert()), "PESTN", String.valueOf(1),
							String.valueOf(pestN2.getStepsPestN()),
							String.valueOf(pestN2.getDp().getCurrentThreshold()),
							String.valueOf(pestN2.getDp().getCurrentStatus().currentResult.toString()) });

					// bestNepthCertifier
					BestNSim bestN1 = new BestNSim(j);
					runBestN(patientlist.get(i).getLevelCert(), bestN1, s == Scenario.S3);
					BestNSim bestN2 = new BestNSim(j);
					runBestN(patientlist.get(i).getLevelCert(), bestN2, s == Scenario.S3);
					writer.writeNext(new String[] { s.name(), String.valueOf(i),
							String.valueOf(patientlist.get(i).getLevelCert()), "BESTN", String.valueOf(0),
							String.valueOf(bestN1.getStepsBestN()),
							String.valueOf(bestN1.getDp().getCurrentThreshold()),
							String.valueOf(bestN1.getDp().getCurrentStatus().currentResult.toString()) });
					writer.writeNext(new String[] { s.name(), String.valueOf(i),
							String.valueOf(patientlist.get(i).getLevelCert()), "BESTN", String.valueOf(1),
							String.valueOf(bestN2.getStepsBestN()),
							String.valueOf(bestN2.getDp().getCurrentThreshold()),
							String.valueOf(bestN2.getDp().getCurrentStatus().currentResult.toString()) });

					// StrictStaircaseDepthCertifier
					StrictSim strict1 = new StrictSim(j);
					runStrict(patientlist.get(i).getLevelCert(), strict1, s == Scenario.S3);
					StrictSim strict2 = new StrictSim(j);
					runStrict(patientlist.get(i).getLevelCert(), strict2, s == Scenario.S3);
					writer.writeNext(new String[] { s.name(), String.valueOf(i),
							String.valueOf(patientlist.get(i).getLevelCert()), "STRICTN", String.valueOf(0),
							String.valueOf(strict1.getStepsStrict()),
							String.valueOf(strict1.getDp().getCurrentThreshold()),
							String.valueOf(strict1.getDp().getCurrentStatus().currentResult.toString()) });
					writer.writeNext(new String[] { s.name(), String.valueOf(i),
							String.valueOf(patientlist.get(i).getLevelCert()), "STRICTN", String.valueOf(1),
							String.valueOf(strict2.getStepsStrict()),
							String.valueOf(strict2.getDp().getCurrentThreshold()),
							String.valueOf(strict2.getDp().getCurrentStatus().currentResult.toString()) });
				}
			}
		}
		writer.close();
	}

	@Test
	public void testRetestAttentionDecCSV() throws IOException {
		ArrayList<Patient> patientlist = new ArrayList<>();
		String filePath = "Test" + System.currentTimeMillis() + ".csv";
		File file = new File(filePath);
		// Create a Workbook
		FileWriter outputfile = new FileWriter(file);
		CSVWriter writer = new CSVWriter(outputfile);
		String[] header = { "IdPatient", "Target", "TestType", "Time", "Steps", "Level", "FinalRes" };
		writer.writeNext(header);
		for (int j = startingLevel; j >= startingLevel; j--) {
			// Generate X Patients with random level certified, at each for loop the maximum
			// level certified decreases
			patientlist.clear();
			for (int i = 0; i < numpatients; i++)
				patientlist.add(new Patient(j));
			// Array to store data and save them to DB
			List<ElementsType> results = new ArrayList<>();
			for (int i = 0; i < numpatients; i++) {
				// PestDepthCertifier
				PestSim pest1 = new PestSim(j);
				runPestAttention(patientlist.get(i).getLevelCert(), pest1);
				PestSim pest2 = new PestSim(j);
				runPestAttention(patientlist.get(i).getLevelCert(), pest2);
				writer.writeNext(new String[] { String.valueOf(i), String.valueOf(patientlist.get(i).getLevelCert()),
						"PEST", String.valueOf(0), String.valueOf(pest1.getStepsPest()),
						String.valueOf(pest1.getDp().getCurrentThreshold()),
						String.valueOf(pest1.getDp().getCurrentStatus().currentResult.toString()) });
				writer.writeNext(new String[] { String.valueOf(i), String.valueOf(patientlist.get(i).getLevelCert()),
						"PEST", String.valueOf(1), String.valueOf(pest2.getStepsPest()),
						String.valueOf(pest2.getDp().getCurrentThreshold()),
						String.valueOf(pest2.getDp().getCurrentStatus().currentResult.toString()) });

				// PestDepthCertifierNew
				PestNSim pestN1 = new PestNSim(j);
				runPestNAttention(patientlist.get(i).getLevelCert(), pestN1);
				PestNSim pestN2 = new PestNSim(j);
				runPestNAttention(patientlist.get(i).getLevelCert(), pestN2);
				writer.writeNext(new String[] { String.valueOf(i), String.valueOf(patientlist.get(i).getLevelCert()),
						"PESTN", String.valueOf(0), String.valueOf(pestN1.getStepsPestN()),
						String.valueOf(pestN1.getDp().getCurrentThreshold()),
						String.valueOf(pestN1.getDp().getCurrentStatus().currentResult.toString()) });
				writer.writeNext(new String[] { String.valueOf(i), String.valueOf(patientlist.get(i).getLevelCert()),
						"PESTN", String.valueOf(1), String.valueOf(pestN2.getStepsPestN()),
						String.valueOf(pestN2.getDp().getCurrentThreshold()),
						String.valueOf(pestN2.getDp().getCurrentStatus().currentResult.toString()) });

				// bestNepthCertifier
				BestNSim bestN1 = new BestNSim(j);
				runBestN(patientlist.get(i).getLevelCert(), bestN1, true);
				BestNSim bestN2 = new BestNSim(j);
				runBestN(patientlist.get(i).getLevelCert(), bestN2, true);
				writer.writeNext(new String[] { String.valueOf(i), String.valueOf(patientlist.get(i).getLevelCert()),
						"BESTN", String.valueOf(0), String.valueOf(bestN1.getStepsBestN()),
						String.valueOf(bestN1.getDp().getCurrentThreshold()),
						String.valueOf(bestN1.getDp().getCurrentStatus().currentResult.toString()) });
				writer.writeNext(new String[] { String.valueOf(i), String.valueOf(patientlist.get(i).getLevelCert()),
						"BESTN", String.valueOf(1), String.valueOf(bestN2.getStepsBestN()),
						String.valueOf(bestN2.getDp().getCurrentThreshold()),
						String.valueOf(bestN2.getDp().getCurrentStatus().currentResult.toString()) });

				// StrictStaircaseDepthCertifier
				StrictSim strict1 = new StrictSim(j);
				runStrict(patientlist.get(i).getLevelCert(), strict1, true);
				StrictSim strict2 = new StrictSim(j);
				runStrict(patientlist.get(i).getLevelCert(), strict2, true);
				writer.writeNext(new String[] { String.valueOf(i), String.valueOf(patientlist.get(i).getLevelCert()),
						"STRICTN", String.valueOf(0), String.valueOf(strict1.getStepsStrict()),
						String.valueOf(strict1.getDp().getCurrentThreshold()),
						String.valueOf(strict1.getDp().getCurrentStatus().currentResult.toString()) });
				writer.writeNext(new String[] { String.valueOf(i), String.valueOf(patientlist.get(i).getLevelCert()),
						"STRICTN", String.valueOf(1), String.valueOf(strict2.getStepsStrict()),
						String.valueOf(strict2.getDp().getCurrentThreshold()),
						String.valueOf(strict2.getDp().getCurrentStatus().currentResult.toString()) });

			}
		}
		// Write the output to a file
	}
}
