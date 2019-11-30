import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './scenario.reducer';
import { IScenario } from 'app/shared/model/scenario.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IScenarioDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ScenarioDetail extends React.Component<IScenarioDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { scenarioEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Scenario [<b>{scenarioEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="scenarioName">Scenario Name</span>
            </dt>
            <dd>{scenarioEntity.scenarioName}</dd>
            <dt>Team</dt>
            <dd>{scenarioEntity.team ? scenarioEntity.team.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/scenario" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/scenario/${scenarioEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ scenario }: IRootState) => ({
  scenarioEntity: scenario.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ScenarioDetail);
