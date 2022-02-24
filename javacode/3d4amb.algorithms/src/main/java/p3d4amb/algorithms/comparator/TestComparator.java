package p3d4amb.algorithms.comparator;


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

import p3d4amb.algorithms.ThresholdCertifier;
import p3d4amb.algorithms.ThresholdCertifier.Solution;


public class TestComparator {

	static final int numpatients = 3000;
	static final int startingLevel = 10;
	static final double probExpectedAnsw = 0.9;

	static final double probExpectedAnswNotCert = 0.9;

	public static void main(String[] args) throws IOException {
		ArrayList<Patient> patientlist = new ArrayList<>();

		// Generate X Patients with random level certified
		for (int i = 0; i < numpatients; i++)
			patientlist.add(new Patient());
		// Array to store data and save them to DB
		List<ElementsType> results = new ArrayList<>();

		for (int i = 0; i < numpatients; i++) {
			// PestDepthCertifier
			PestSim pest1 = new PestSim(startingLevel);
//			runPest(patientlist, i, pest1);

			// PestDepthCertifierNew
			Pest3Sim pestNew1 = new Pest3Sim(startingLevel);
			runPestNew(patientlist, i, pestNew1);

			// Best3DepthCertifier
			Best3DSim best3D1 = new Best3DSim(startingLevel);
//			runBest3D(patientlist, i, best3D1);

			// StrictStaircaseDepthCertifier
			StrictSim strict1 = new StrictSim(startingLevel);
//			runStrict(patientlist, i, strict1);

			results.add(new ElementsType(patientlist.get(i).getId(), patientlist.get(i).getLevelCert(), pest1, pestNew1,
					best3D1, strict1));

		}
		// Save data to excel file
		saveToExcel(results, startingLevel);

	}

	private static void runStrict(ArrayList<Patient> patientlist, int i, StrictSim strict) {
		ThresholdCertifier.Solution solution;
		do {
			// patient not certified
			if (patientlist.get(i).getLevelCert() == 0)
				solution = getSolutionRandom(Solution.WRONG, probExpectedAnswNotCert);
			else {
				if (strict.dp.getCurrentThreshold() >= patientlist.get(i).getLevelCert())
					solution = getSolutionRandom(Solution.RIGHT, probExpectedAnsw);
				else
					solution = getSolutionRandom(Solution.WRONG, probExpectedAnsw);
			}
			strict.dp.computeNextThreshold(solution);
			strict.stepsStrict++;
		} while (strict.dp.getCurrentStatus().currentResult.equals(CONTINUE));
	}

	private static void runBest3D(ArrayList<Patient> patientlist, int i, Best3DSim best3d) {
		ThresholdCertifier.Solution solution;
		do {
			// patient not certified
			if (patientlist.get(i).getLevelCert() == 0)
				solution = getSolutionRandom(Solution.WRONG, probExpectedAnswNotCert);
			else {
				if (best3d.dp.getCurrentThreshold() >= patientlist.get(i).getLevelCert())
					solution = getSolutionRandom(Solution.RIGHT, probExpectedAnsw);
				else
					solution = getSolutionRandom(Solution.WRONG, probExpectedAnsw);
			}
			best3d.dp.computeNextThreshold(solution);
			best3d.stepsBest3D++;
		} while (best3d.dp.getCurrentStatus().currentResult.equals(CONTINUE));
	}

	private static void runPestNew(ArrayList<Patient> patientlist, int i, Pest3Sim pestNew) {
		ThresholdCertifier.Solution solution;
		do {
			// patient not certified
			if (patientlist.get(i).getLevelCert() == 0)
				solution = getSolutionRandom(Solution.WRONG, probExpectedAnswNotCert);
			else {
				if (pestNew.dp.getCurrentThreshold() >= patientlist.get(i).getLevelCert())
					solution = getSolutionRandom(Solution.RIGHT, probExpectedAnsw);
				else
					solution = getSolutionRandom(Solution.WRONG, probExpectedAnsw);
			}
			pestNew.dp.computeNextThreshold(solution);
			pestNew.stepsPestNew++;
		} while (pestNew.dp.getCurrentStatus().currentResult.equals(CONTINUE));
	}

	private static void runPest(ArrayList<Patient> patientlist, int i, PestSim pest) {
		ThresholdCertifier.Solution solution;
		do {
			// patient not certified
			if (patientlist.get(i).getLevelCert() == 0)
				solution = getSolutionRandom(Solution.WRONG, probExpectedAnswNotCert);
			else {
				if (pest.dp.getCurrentThreshold() >= patientlist.get(i).getLevelCert())
					solution = getSolutionRandom(Solution.RIGHT, probExpectedAnsw);
				else
					solution = getSolutionRandom(Solution.WRONG, probExpectedAnsw);
			}
			pest.dp.computeNextThreshold(solution);
			pest.stepsPest++;
		} while (pest.dp.getCurrentStatus().currentResult.equals(CONTINUE));
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
	 * "stepPest2", "stepPestNew1", "stepPestNew2", "stepBest3D1", "stepBest3D2",
	 * "stepStrict1", "stepStrict2", "targetLevel", "levelPest1", "levelPest2",
	 * "levelPestNew1", "levelPestNew2", "levelBest3D1", "levelBest3D2",
	 * "levelStrict1", "levelStrict2", "resPest1", "resPest2", "resPestNew1",
	 * "resPestNew2", "resBest3D1", "resBest3D2", "resStrict1", "resStrict2" because
	 * of test retest
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
		String[] columns = { "idPatient", "stepPest1", "stepPestNew1", "stepBest3D1", "stepStrict1", "targetLevel",
				"levelPest1", "levelPestNew1", "levelBest3D1", "levelStrict1", "resPest1", "resPestNew1", "resBest3D1",
				"resStrict1" };

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
			row.createCell(cellnum++).setCellValue(singleres.getPestNew1().getStepsPestNew());
			row.createCell(cellnum++).setCellValue(singleres.getBest3d1().getStepsBest3D());
			row.createCell(cellnum++).setCellValue(singleres.getStrict1().getStepsStrict());
			// if not cert set color to red and set target level at starting level value
			int targetcellnum = cellnum++;
			if (singleres.getTargetLevel() == 0) {
				Cell dateOfBirthCell = row.createCell(targetcellnum);
				dateOfBirthCell.setCellValue(singleres.getTargetLevel());
				dateOfBirthCell.setCellStyle(notCertCellStyle);
			} else
				row.createCell(targetcellnum).setCellValue(singleres.getTargetLevel());

			row.createCell(cellnum++).setCellValue(singleres.getPest1().getDp().getCurrentThreshold());
			row.createCell(cellnum++).setCellValue(singleres.getPestNew1().getDp().getCurrentThreshold());
			row.createCell(cellnum++).setCellValue(singleres.getBest3d1().getDp().getCurrentThreshold());
			row.createCell(cellnum++).setCellValue(singleres.getStrict1().getDp().getCurrentThreshold());
			row.createCell(cellnum++)
					.setCellValue(singleres.getPest1().getDp().getCurrentStatus().currentResult.toString());
			row.createCell(cellnum++)
					.setCellValue(singleres.getPestNew1().getDp().getCurrentStatus().currentResult.toString());
			row.createCell(cellnum++)
					.setCellValue(singleres.getBest3d1().getDp().getCurrentStatus().currentResult.toString());
			row.createCell(cellnum++)
					.setCellValue(singleres.getStrict1().getDp().getCurrentStatus().currentResult.toString());
		}

		// Resize all columns to fit the content size
		for (int i = 0; i < columns.length; i++) {
			sheet.autoSizeColumn(i);
		}

		// Write the output to a file
		FileOutputStream fileOut = new FileOutputStream("simulation" + System.currentTimeMillis() + ".xlsx");
		workbook.write(fileOut);
		fileOut.close();

		// Closing the workbook
		workbook.close();

	}
}
