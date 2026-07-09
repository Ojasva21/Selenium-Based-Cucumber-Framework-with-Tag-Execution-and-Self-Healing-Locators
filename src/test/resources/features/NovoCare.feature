@novocare
Feature: NovoCare Self Healing Framework

  Background:
    Given User launches NovoCare website
    And User accepts cookies

  @smoke
  @home
  Scenario: Verify Home Navigation

    When User clicks Home
    Then Home page should open

  @regression
  @products
  Scenario: Verify Products Navigation

    When User opens Products menu
    And User clicks NovoFine Needles
    Then NovoFine Needles page should open

  @regression
  @offerings
  Scenario: Verify Product Offerings

    When User clicks See Product Offerings
    Then Product selection drawer should be displayed