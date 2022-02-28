package p3d4amb.algorithms;

import static p3d4amb.algorithms.ThresholdCertifier.Result.CONTINUE;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import p3d4amb.algorithms.ThresholdCertifier;
import p3d4amb.algorithms.ThresholdCertifier.Solution;
import p3d4amb.algorithms.comparator.BestNSim;
import p3d4amb.algorithms.comparator.ElementsType;
import p3d4amb.algorithms.comparator.Patient;
import p3d4amb.algorithms.comparator.PestNSim;
import p3d4amb.algorithms.comparator.PestSim;
import p3d4amb.algorithms.comparator.StrictSim;

public class TestComparatorTestRetest {

	static final int numpatients = 3000;
	static final int startingLevel = 15;
	static final double probExpectedAnsw = 0.95;

	static final double probExpectedAnswNotCert = 0.95;

	@Test
	public void testRetest() throws IOException {
		ArrayList<Patient> patientlist = new ArrayList<>();

		// Generate X Patients with random level certified
		for (int i = 0; i < numpatients; i++)
			patientlist.add(new Patient());
		// Array to store data and save them to DB
		List<ElementsType> results = new ArrayList<>();

		for (int i = 0; i < numpatients; i++) {
			// PestDepthCertifier
			PestSim pest1 = new PestSim(startingLevel);
			runPest(patientlist.get(i).getLevelCert(), pest1);
			PestSim pest2 = new PestSim(startingLevel);
			runPest(patientlist.get(i).getLevelCert(), pest2);

			// PestDepthCertifierNew
			PestNSim pestNew1 = new PestNSim(startingLevel);
			runPestNew(patientlist.get(i).getLevelCert(), pestNew1);
			PestNSim pestNew2 = new PestNSim(startingLevel);
			runPestNew(patientlist.get(i).getLevelCert(), pestNew2);

			// Best3DepthCertifier
			BestNSim best3D1 = new BestNSim(startingLevel);
			runBest3D(patientlist.get(i).getLevelCert(), best3D1);
			BestNSim best3D2 = new BestNSim(startingLevel);
			runBest3D(patientlist.get(i).getLevelCert(), best3D2);

			// StrictStaircaseDepthCertifier
			StrictSim strict1 = new StrictSim(startingLevel);
			runStrict(patientlist.get(i).getLevelCert(), strict1);
			StrictSim strict2 = new StrictSim(startingLevel);
			runStrict(patientlist.get(i).getLevelCert(), strict2);

			results.add(new ElementsType(patientlist.get(i).getId(), patientlist.get(i).getLevelCert(), pest1, pest2,
					pestNew1, pestNew2, best3D1, best3D2, strict1, strict2));

		}
		// Save data to excel file
		saveToExcel(results, startingLevel);

	}

	private static void runStrict(int patientLevelCert, StrictSim strict) {
		ThresholdCertifier.Solution solution;
		do {
			// patient not certified
			if (patientLevelCert == 0)
				solution = getSolutionRandom(Solution.WRONG, probExpectedAnswNotCert);
			else {
				if (strict.getDp().getCurrentThreshold() >= patientLevelCert)
					solution = getSolutionRandom(Solution.RIGHT, probExpectedAnsw);
				else
					solution = getSolutionRandom(Solution.WRONG, probExpectedAnsw);
			}
			strict.getDp().computeNextThreshold(solution);
			strict.setStepsStrict(strict.getStepsStrict() + 1);
		} while (strict.getDp().getCurrentStatus().currentResult.equals(CONTINUE));
	}

	private static void runBest3D(int patientLevelCert, BestNSim best3d) {
		ThresholdCertifier.Solution solution;
		do {
			// patient not certified
			if (patientLevelCert == 0)
				solution = getSolutionRandom(Solution.WRONG, probExpectedAnswNotCert);
			else {
				if (best3d.getDp().getCurrentThreshold() >= patientLevelCert)
					solution = getSolutionRandom(Solution.RIGHT, probExpectedAnsw);
				else
					solution = getSolutionRandom(Solution.WRONG, probExpectedAnsw);
			}
			best3d.getDp().computeNextThreshold(solution);
			best3d.setStepsBestN(best3d.getStepsBestN() + 1);
		} while (best3d.getDp().getCurrentStatus().currentResult.equals(CONTINUE));
	}

	private static void runPestNew(int patientLevelCert, PestNSim pestNew) {
		ThresholdCertifier.Solution solution;
		do {
			// patient not certified
			if (patientLevelCert == 0)
				solution = getSolutionRandom(Solution.WRONG, probExpectedAnswNotCert);
			else {
				if (pestNew.getDp().getCurrentThreshold() >= patientLevelCert)
					solution = getSolutionRandom(Solution.RIGHT, probExpectedAnsw);
				else
					solution = getSolutionRandom(Solution.WRONG, probExpectedAnsw);
			}
			pestNew.getDp().computeNextThreshold(solution);
			pestNew.setStepsPestN(pestNew.getStepsPestN() + 1);
		} while (pestNew.getDp().getCurrentStatus().currentResult.equals(CONTINUE));
	}

	private static void runPest(int patientLevelCert, PestSim pest) {
		ThresholdCertifier.Solution solution;
		do {
			// patient not certified
			if (patientLevelCert == 0)
				solution = getSolutionRandom(Solution.WRONG, probExpectedAnswNotCert);
			else {
				if (pest.getDp().getCurrentThreshold() >= patientLevelCert)
					solution = getSolutionRandom(Solution.RIGHT, probExpectedAnsw);
				else
					solution = getSolutionRandom(Solution.WRONG, probExpectedAnsw);
			}
			pest.getDp().computeNextThreshold(solution);
			pest.setStepsPest(pest.getStepsPest() + 1);
		} while (pest.getDp().getCurrentStatus().currentResult.equals(CONTINUE));
	}

	/**
	 * Generate a random answer between RIGHT and WRONG
	 */
	public static Solution getSolutionRandom(Solution desiredSol, double d) {
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
	 * "stepPest2", "stepPestNew1", "stepPestNew2", "stepBest3D1", "stepBest3D2",
	 * "stepStrict1", "stepStrict2", "targetLevel", "levelPest1", "levelPest2",
	 * "levelPestNew1", "levelPestNew2", "levelBest3D1", "levelBest3D2",
	 * "levelStrict1", "levelStrict2", "resPest1", "resPest2", "resPestNew1",
	 * "resPestNew2", "resBest3D1", "resBest3D2", "resStrict1", "resStrict2"
	 * because of test retest
	 */
	static void saveToExcel(List<ElementsType> results, int startingLevel) throws IOException {
		// Create a Workbook
		Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

		// Create a Sheet
		Sheet sheet = workbook.createSheet("Simulation");

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
		String[] columns = { "idPatient", "stepPest1", "stepPest2", "stepPestNew1", "stepPestNew2", "stepBest3D1",
				"stepBest3D2", "stepStrict1", "stepStrict2", "targetLevel", "levelPest1", "levelPest2", "levelPestNew1",
				"levelPestNew2", "levelBest3D1", "levelBest3D2", "levelStrict1", "levelStrict2", "resPest1", "resPest2",
				"resPestNew1", "resPestNew2", "resBest3D1", "resBest3D2", "resStrict1", "resStrict2" };

		// Create cells
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

		// Write the output to a file
		FileOutputStream fileOut = new FileOutputStream("simulationTestRetest" + System.currentTimeMillis() + ".xlsx");
		workbook.write(fileOut);
		fileOut.close();

		// Closing the workbook
		workbook.close();

	}
}
